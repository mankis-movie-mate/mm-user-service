package cz.cvut.moviemate.userservice.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal")
public class InternalServiceController {

    /**
     * Always returns HTTP 500 (Internal Server Error)
     */
    @GetMapping("/always-error")
    public ResponseEntity<String> alwaysError() {
        // You can return a ResponseEntity with status 500 and message
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Intentional error for circuit breaker test!");
    }

    /**
     * Returns HTTP 200 after a delay (simulates slow backend)
     */
    @GetMapping("/slow")
    public ResponseEntity<String> slow(@RequestParam(defaultValue = "3000") long ms) throws InterruptedException {
        Thread.sleep(ms);
        return ResponseEntity.ok("slow OK");
    }
}