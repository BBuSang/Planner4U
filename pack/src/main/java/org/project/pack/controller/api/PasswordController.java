package org.project.pack.controller.api;

import org.project.pack.entity.User;
import org.project.pack.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/pw")
public class PasswordController {

	@Autowired
	UserRepository userRep;

	@Autowired
	PasswordEncoder passwordEncoder;

	@PostMapping("/check-email")
	public ResponseEntity<String> checkEmail(@RequestParam(name = "email") String email) {
		User user = userRep.findByEmail(email);
		if (user != null) {
			// 이메일이 존재하지만 '회원가입'인 경우
			if (!"회원가입".equals(user.getProvider())) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("CAN NOT CHANGE");
			}
			// 이메일이 존재하고 '회원가입'이 아닌 경우
			return ResponseEntity.ok("you can change");
		}
		// 이메일이 존재하지 않는 경우
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("DO NOT CHANGE");
	}

	@PostMapping("/changePassword")
	public ModelAndView changePassword(@RequestParam(name = "email") String email,
			@RequestParam(name = "newPassword") String pwd) {
		User user = userRep.findByEmail(email);
		user.setPwd(passwordEncoder.encode(pwd));
		userRep.save(user);
		return new ModelAndView("redirect:/app/login");

	}
}













