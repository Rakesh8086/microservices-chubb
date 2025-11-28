package com.quiz.quizApp.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.quiz.quizApp.model.Question;
import com.quiz.quizApp.model.QuestionWrapper;
import com.quiz.quizApp.model.Quiz;
import com.quiz.quizApp.model.Response;
import com.quiz.quizApp.repository.QuestionRepositoy;
import com.quiz.quizApp.repository.QuizRepository;

@Service
public class QuizService {
	@Autowired
	QuizRepository quizRepo;
	
	@Autowired
	QuestionRepositoy questionRepo;

	public ResponseEntity<Quiz> createQuiz(String category, int numQ, String title) {
		Quiz quiz = new Quiz();
		quiz.setTitle(title);
		List<Question> questions = questionRepo.findRandomQuestionByCategory(category, numQ);
		quiz.setQuestions(questions);
		quizRepo.save(quiz);
		
		return new ResponseEntity<>(quiz,HttpStatus.CREATED);
	}

	public ResponseEntity<List<QuestionWrapper>> getQuiz(String quizId) {

        Optional<Quiz> quizOptional = quizRepo.findById(quizId);

        if (quizOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Quiz quiz = quizOptional.get();
   
        // Convert each Question into a QuestionWrapper
        List<QuestionWrapper> wrappedQuestions = quiz.getQuestions()
                .stream()
                .map(ques -> new QuestionWrapper(
                        ques.getId(),
                        ques.getQuestionTitle(),
                        ques.getOption1(),
                        ques.getOption2(),
                        ques.getOption3(),
                        ques.getOption4()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(wrappedQuestions);
    }

	public ResponseEntity<Integer> submitQuiz(String quizId, List<Response> responses) {
	    Optional<Quiz> quizOpt = quizRepo.findById(quizId);
	    
	    if(quizOpt.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }

	    Quiz quiz = quizOpt.get();
	    List<Question> questions = quiz.getQuestions();

	    // Convert responses list to a Map for quick lookup: {questionId -> userAnswer}
	    Map<String, String> responseMap = responses.stream()
	            .collect(Collectors.toMap(Response::getId, Response::getRightAnswer));

	    int score = 0;

	    // Compare correct answers
	    for(Question ques : questions) {
	        String userAnswer = responseMap.get(ques.getId());
	        if (userAnswer != null && userAnswer.equalsIgnoreCase(ques.getRightAnswer())) {
	            score++;
	        }
	    }

	    return ResponseEntity.ok(score);
	}
	
}
