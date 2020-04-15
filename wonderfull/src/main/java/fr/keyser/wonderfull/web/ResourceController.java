package fr.keyser.wonderfull.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResourceController {

	@GetMapping({ "/css/*.*", "/js/*.*", "/fonts/*.*", "/" })
	public ResponseEntity<Resource> getResource(HttpServletRequest request) {
		String uri = request.getRequestURI();
		uri = uri.replaceAll("\\.\\.", "");

		if ("/".equals(uri))
			uri = "/index.html";

		Resource relative = new ClassPathResource("/dist" + uri);
		if (relative.exists()) {
			MediaType mediaType = MediaType.TEXT_HTML;
			if (uri.endsWith(".css"))
				mediaType = new MediaType("text", "css");
			else if (uri.endsWith(".js"))
				mediaType = new MediaType("application", "javascript");
			else if (uri.endsWith(".woff2"))
				mediaType = new MediaType("font", "woff2");

			return ResponseEntity.status(HttpStatus.OK).contentType(mediaType).body(relative);
		} else
			return ResponseEntity.notFound().build();
	}
}
