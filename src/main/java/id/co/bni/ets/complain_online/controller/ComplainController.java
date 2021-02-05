package id.co.bni.ets.complain_online.controller;

import id.co.bni.ets.complain_online.model.Card;
import id.co.bni.ets.complain_online.model.Complain;
import id.co.bni.ets.complain_online.model.MentionDetail;
import id.co.bni.ets.complain_online.service.ComplainService;
import id.co.bni.ets.complain_online.service.TwitterService;
import id.co.bni.ets.lib.model.ApiResponse;
import id.co.bni.ets.lib.util.UserIdUtil;
import java.util.List;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 *
 * @author Juang Nasution
 */
@RestController
@RequestMapping("/complain")
public class ComplainController {

    private final ComplainService complainService;
    private final TwitterService twitterService;

    public ComplainController(ComplainService complainService, TwitterService twitterService) {
        this.complainService = complainService;
        this.twitterService = twitterService;
    }

    @GetMapping()
    public Page<Complain> getComplain(@RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String category,
            OAuth2Authentication authentication,
            Pageable pageable) {
        return complainService.getComplain(UserIdUtil.getUserId(authentication), searchTerm, pageable);
    }

    @GetMapping("/atm")
    public Page<Complain> getComplainAtm(@RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String category,
            Pageable pageable) {
        return complainService.getComplainAtm(searchTerm, category, pageable);
    }

    @GetMapping("/echannel")
    public Page<Complain> getComplainechannel(@RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String category, Pageable pageable) {
        return complainService.getComplainAtm(searchTerm, category, pageable);
    }

    @GetMapping("/{id}")
    public ApiResponse<Complain> getComplainDetail(@Valid @PathVariable String id, OAuth2Authentication authentication) {
        return ApiResponse.apiOk(complainService.getDetailComplain(UserIdUtil.getUserId(authentication), id));
    }

    @PostMapping
    public ApiResponse<?> createComplain(@Valid @RequestBody Complain complain, OAuth2Authentication authentication) {
        complainService.createComplain(UserIdUtil.getUserId(authentication), complain);
        return ApiResponse.apiOk("sukses");
    }

    @PutMapping("/{id}")
    public ApiResponse<?> updateComplain(@Valid @PathVariable String id,
            @RequestBody String responseComplain) {
        complainService.responseComplain(id, responseComplain);
        return ApiResponse.apiOk("suksesk");
    }

    @GetMapping("/card")
    public ApiResponse<List<Card>> getCard(@RequestParam(required = false) String searchTerm, OAuth2Authentication authentication) {
        return ApiResponse.apiOk(complainService.getCard(UserIdUtil.getUserId(authentication), searchTerm));
    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> getReport() {
        String fileName = complainService.getFileName();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(fileName)
                .build();
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        headers.setContentDisposition(contentDisposition);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(complainService.getReport(), headers, HttpStatus.OK);
    }

    @GetMapping("/twitter")
    public List<MentionDetail> getMention() throws TwitterException {
        return twitterService.getMention();
    }

    @GetMapping("/dm")
    public List<twitter4j.DirectMessage> dm() throws TwitterException {
        return twitterService.directMessage();
    }

    @GetMapping("/sendDm")
    public String sendDm(@RequestParam String name) throws TwitterException {
        return twitterService.sendDirectMessage(name);
    }
}
