package com.quiz.quizApp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quiz.quizApp.model.Quiz;

public interface QuizRepository extends MongoRepository<Quiz,String>{

}
