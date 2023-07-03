package com.interview.bookstore.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.bookstore.domain.Book;
import com.interview.bookstore.model.Result;
import com.interview.bookstore.model.RootResults;
import com.interview.bookstore.repository.BookRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Service Implementation for managing {@link Book}.
 */
@Service
@Transactional
@PropertySource("external-rest-provider.properties")
public class BookService {

    private final Logger log = LoggerFactory.getLogger(BookService.class);

    private final BookRepository bookRepository;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;


    @Value("${api-key}")
    private String apiKey;
    @Value("${base-url}")
    private String url;



    public BookService(BookRepository bookRepository, WebClient webClient, ObjectMapper objectMapper) {
        this.bookRepository = bookRepository;
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    /**
     * Save a book.
     *
     * @param book the entity to save.
     * @return the persisted entity.
     */
    public Book save(Book book) {
        log.debug("Request to save Book : {}", book);
        return bookRepository.save(book);
    }

    /**
     * Update a book.
     *
     * @param book the entity to save.
     * @return the persisted entity.
     */
    public Book update(Book book) {
        log.debug("Request to save Book : {}", book);
        return bookRepository.save(book);
    }

    /**
     * Partially update a book.
     *
     * @param book the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Book> partialUpdate(Book book) {
        log.debug("Request to partially update Book : {}", book);

        return bookRepository
            .findById(book.getId())
            .map(existingBook -> {
                if (book.getTitle() != null) {
                    existingBook.setTitle(book.getTitle());
                }
                if (book.getPrice() != null) {
                    existingBook.setPrice(book.getPrice());
                }

                return existingBook;
            })
            .map(bookRepository::save);
    }

    /**
     * Get all the books.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Book> findAll(Pageable pageable) {
        log.debug("Request to get all Books");
        return bookRepository.findAll(pageable);
    }

    /**
     * Get all the books with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Book> findAllWithEagerRelationships(Pageable pageable) {
        return bookRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one book by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Book> findOne(Long id) {
        log.debug("Request to get Book : {}", id);
        return bookRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the book by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Book : {}", id);
        bookRepository.deleteById(id);
    }

    public Page<Book> findCheapBooks(Pageable pageable) {
        List<Book> books = bookRepository.findAll().stream().filter(book -> book.getPrice() <= 20).collect(Collectors.toList());
        return new PageImpl<>(books, pageable, books.size());
    }

    public List<String> findBooksByAuthor(String authorName) throws JsonProcessingException {
        String queryParams = "api-key=" + apiKey + "&author=" + authorName;
        String payload = url + queryParams;
        String jsonResponse = getJsonResponseFromExternalApi(payload);
        RootResults rootResults = objectMapper.readValue(jsonResponse, RootResults.class);
        return rootResults.results.stream().map(Result::getBook_title).collect(Collectors.toList());
    }

    private String getJsonResponseFromExternalApi(String url) {
        return webClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

}
