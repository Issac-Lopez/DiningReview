package com.example.diningreview.controller;

import com.example.diningreview.model.AdminReviewStatus;
import com.example.diningreview.model.DiningReview;
import com.example.diningreview.repositories.RestaurantRepo;
import com.example.diningreview.repositories.ReviewRepo;
import com.example.diningreview.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

@RestController
public class DiningReviewController {

    @Autowired
    private ReviewRepo reviewRepo;
    @Autowired
    private RestaurantRepo restaurantRepo;
    @Autowired
    private UserRepo userRepo;

    @PostMapping("/dining-review")
    public DiningReview createNewDiningreview(@RequestBody DiningReview diningReview){
        if(restaurantRepo.findById(diningReview.getRestaurantId()).isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This restaurant does not exist");
        }
        if(userRepo.findByUserName(diningReview.getReviewedBy()).isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This user does not exist");
        }
        return reviewRepo.save(diningReview);
    }

    @GetMapping("/dining-review")
    public Iterable<DiningReview> getAllDiningReviews(){
        return reviewRepo.findAll();

    }

    @GetMapping("/dining-review/pending")
    public Iterable<DiningReview> getPendingReviews(){
        return reviewRepo.findByAdminReviewStatus(AdminReviewStatus.PENDING);
    }

    @GetMapping("/dining-review/accepted/{id}")
    public Iterable<DiningReview> getAcceptedReviewByRestaurantId(@PathVariable("id") Long id){
        return reviewRepo.findByIdAndAdminReviewStatus(id, AdminReviewStatus.APPROVED);
    }

    @PutMapping("/dining-review/{id}/approve")
    public DiningReview approveReview(@PathVariable("id") Long id){
        Optional<DiningReview> reviewOptional = reviewRepo.findById(id);
        if(reviewOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This review does not exist");
        }
        DiningReview reviewToApprove = reviewOptional.get();
        reviewToApprove.setAdminReviewStatus(AdminReviewStatus.APPROVED);
        return reviewRepo.save(reviewToApprove);
    }

    @PutMapping("/dining-review/{id}/reject")
    public DiningReview rejectReview(@PathVariable("id") Long id){
        Optional<DiningReview> reviewOptional = reviewRepo.findById(id);
        if(reviewOptional.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This review does not exist");
        }
        DiningReview reviewToReject = reviewOptional.get();
        reviewToReject.setAdminReviewStatus(AdminReviewStatus.REJECTED);
        return reviewRepo.save(reviewToReject);
    }
}