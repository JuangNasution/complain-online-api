package id.co.bni.ets.complain_online.controller;

import id.co.bni.ets.complain_online.model.Card;
import id.co.bni.ets.complain_online.model.Complain;
import id.co.bni.ets.complain_online.model.ComplainTwitter;
import id.co.bni.ets.complain_online.model.MentionDetail;
import id.co.bni.ets.complain_online.service.ComplainService;
import id.co.bni.ets.complain_online.service.TwitterService;
import id.co.bni.ets.lib.model.ApiResponse;
import id.co.bni.ets.lib.util.UserIdUtil;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @PostMapping
    public ApiResponse<?> createComplain(@Valid @RequestBody Complain complain, OAuth2Authentication authentication) {
        complainService.createComplainApp(UserIdUtil.getUserId(authentication), complain);
        return ApiResponse.apiOk("sukses");
    }

    @PutMapping("/{id}")
    public ApiResponse<?> updateComplain(@Valid @PathVariable String id,
            @RequestBody Complain complain) {
        complainService.responseComplainApp(id, complain);
        return ApiResponse.apiOk("suksesk");
    }

    @GetMapping("/response")
    public Page<Complain> getResponse(@RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String category, Pageable pageable) {
        return complainService.getComplainApp(searchTerm, category, pageable);
    }

    @GetMapping("/export")
    public Page<Complain> getExport(@RequestParam(required = false)
            @DateTimeFormat(pattern = "MM-dd-yyyy") Date fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "MM-dd-yyyy") Date toDate,
            @RequestParam(required = false) String category, Pageable pageable) {
        return complainService.getExportApp(fromDate, toDate, category, pageable);
    }

    @GetMapping("/{id}")
    public ApiResponse<Complain> getComplainDetail(@Valid @PathVariable String id, OAuth2Authentication authentication) {
        return ApiResponse.apiOk(complainService.getDetailComplainApp(UserIdUtil.getUserId(authentication), id));
    }

    @GetMapping("/card")
    public ApiResponse<List<Card>> getCard(@RequestParam(required = false) String searchTerm, OAuth2Authentication authentication) {
        return ApiResponse.apiOk(complainService.getCard(UserIdUtil.getUserId(authentication), searchTerm));
    }

    @GetMapping("/report")
    public ResponseEntity<byte[]> getReport(@RequestParam(required = false)
            @DateTimeFormat(pattern = "MM-dd-yyyy") Date fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "MM-dd-yyyy") Date toDate,
            @RequestParam(required = false) String category) {
        String fileName = complainService.getFileName(category);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                .filename(fileName)
                .build();
        headers.add(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION);
        headers.setContentDisposition(contentDisposition);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(complainService.getReport(fromDate, toDate, category), headers, HttpStatus.OK);
    }

    @GetMapping("/twitter")
    public List<MentionDetail> getMention() throws TwitterException {
        return twitterService.getMention();
    }

    @GetMapping("/twitter/{id}")
    public ApiResponse<ComplainTwitter> getComplainDetailTwt(@Valid @PathVariable String id) {
        return ApiResponse.apiOk(complainService.getDetailComplainTwt(id));
    }

    @PostMapping("/twitter")
    public ApiResponse<?> createComplainTwt(@Valid @RequestBody ComplainTwitter complain) {
        complainService.createComplainTwt(complain);
        return ApiResponse.apiOk("sukses");
    }

    @GetMapping("/response-twitter")
    public Page<ComplainTwitter> getResponseTwt(@RequestParam(required = false) String searchTerm,
            @RequestParam(required = false) String category, Pageable pageable) {
        return complainService.getComplainTwt(searchTerm, category, pageable);
    }

    @PutMapping("twitter/{id}")
    public ApiResponse<?> updateComplainTwt(@Valid @PathVariable String id,
            @RequestBody ComplainTwitter complain) {
        complainService.responseComplainTwt(id, complain);
        return ApiResponse.apiOk("sukses");
    }

    @PutMapping("drop-twitter/{id}")
    public ApiResponse<?> dropTwt(@Valid @PathVariable String id) {
        complainService.dropTwt(id);
        return ApiResponse.apiOk("sukses");
    }

//    @GetMapping("/sendDm")
//    public String sendDm(@RequestParam String name) throws TwitterException {
//        return twitterService.sendDirectMessage(name);
//    }
}
