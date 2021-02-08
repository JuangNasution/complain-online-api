package id.co.bni.ets.complain_online.service;

import id.co.bni.ets.complain_online.model.ComplainTwitter;
import id.co.bni.ets.complain_online.model.MentionDetail;
import id.co.bni.ets.complain_online.repository.ProcessedTwitterRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import twitter4j.DirectMessage;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

/**
 *
 * @author Juang Nasution
 */
@Service
public class TwitterService {

    private final Twitter twitter = TwitterFactory.getSingleton();
    private final ProcessedTwitterRepository twitterRepository;

    public TwitterService(ProcessedTwitterRepository twitterRepository) {
        this.twitterRepository = twitterRepository;
    }

    public List<MentionDetail> getMention() throws TwitterException {
        List<String> twitterList = twitterRepository.findProcessedTwitter();
        for (String string : twitterList) {
            System.out.println(string);
        }
        List<MentionDetail> mentionDetails = new ArrayList<>();
        for (Status tweet : twitter.getMentionsTimeline()) {
            System.out.println(String.valueOf(tweet.getId()));
            if (!twitterList.contains(String.valueOf(tweet.getId()))) {
                MentionDetail mentionDetail = new MentionDetail();
                mentionDetail.setId(String.valueOf(tweet.getId()));
                mentionDetail.setText(tweet.getText());
                mentionDetail.setUser(tweet.getUser().getScreenName());
                mentionDetail.setImage(tweet.getUser().get400x400ProfileImageURLHttps());
                mentionDetail.setCreatedAt(tweet.getCreatedAt());
                mentionDetails.add(mentionDetail);
            }
        }
        return mentionDetails;
    }

    public String sendDirectMessage(ComplainTwitter ct) throws TwitterException {
        TwitterFactory factory = new TwitterFactory();
        Twitter twitterr = factory.getInstance();
        String text = String.format("Hai %1$s "
                + "\n\n Berdasarkan complain kamu tentang : %2$s "
                + "\n Dengan detail berikut : %3$s "
                + "\n Berikut adalah response dari TIM kami : %4$s "
                + "\n\n Jika masih ada hal yang kurang jelas silahkan kirim complain kembali atau masuk ke "
                + " portal keluhan BNI di halaman berikut : localhost:4200 "
                + "\n\n Thanks, BNI TEAM", ct.getUsername(), ct.getSubject(), ct.getComplainDetail(),
                ct.getComplainResponse());
        DirectMessage directMessage = twitterr.sendDirectMessage(ct.getUsername(), text); //        DirectMessage message = twitter.sendDirectMessage(name, "ini testing API");
        return directMessage.getText();
    }
}
