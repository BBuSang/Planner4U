package org.project.pack.controller.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import org.project.pack.entity.User;
import org.project.pack.repository.UserRepository;
import org.project.pack.services.UDS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

@RestController
@RequestMapping("/api")
public class SigninController {
	
	@Autowired
	UserRepository userRep;
	
    @Autowired
    private UDS userService;
    
    @Autowired
    PasswordEncoder passwordEncoder;
        
    @Value("${auth.user}")
    String userAuth;
    
    @PostMapping("/signin")
    public ModelAndView signin(
    		@RequestParam(name="email") String eamil,
    		@RequestParam(name="name") String name,
    		@RequestParam(name="password") String pwd
    		) {
    	User user = new User();
    	user.setEmail(eamil);
    	user.setName(name);
    	user.setPwd(passwordEncoder.encode(pwd));
    	user.setProvider("회원가입");
    	user.setAuths(List.of(userAuth));
    	userRep.save(user);
    	
    	return new ModelAndView("redirect:/app/login");
    }
    
    @PostMapping("/signin/email")
    public ResponseEntity<String> emailCheck(@RequestParam(name="email") String email) {
        try {
            boolean exists = userService.isEmailExists(email);
            if (exists) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
            } else {
                return ResponseEntity.ok("Email is available");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
        }
    }
    
    
}
