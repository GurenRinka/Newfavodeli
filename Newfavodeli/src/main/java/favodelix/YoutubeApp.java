package favodelix;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTube.Search;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

public class YoutubeApp {

	public String vid;
	public String title;
	public String samune;

	public YoutubeApp() {
	}

	public YoutubeApp(String vid, String title, String samune) {
		super();
		this.vid = vid;
		this.title = title;
		this.samune = samune;
	}

	/** Global instance properties filename. */
	private static String PROPERTIES_FILENAME = "youtube.properties";

	/** Global instance of the HTTP transport. */
	private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

	/** Global instance of the JSON factory. */
	private static final JsonFactory JSON_FACTORY = new JacksonFactory();

	/** Global instance of the max number of videos we want returned (50 = upper limit per page). */
	private static final long NUMBER_OF_VIDEOS_RETURNED = 1;

	/** Global instance of Youtube object to make all API requests. */
	private static YouTube youtube;

	//YoutubeのチャンネルIDを引数として貰ってくる
	public List<SearchResult> youtubelive(String YTChannnelID) {
		List<SearchResult> searchResultList = null; 				//youtube.javaに返すリスト

		Properties properties = new Properties();
		//APIキーが記載されているyoutube.propertiesを読み込む
		try {
			InputStream in = Search.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
			properties.load(in);
		} catch (IOException e) {
			System.err.println("There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause()
			+ " : " + e.getMessage());
			System.exit(1);
		}
		//Youtubeのサンプルを利用して条件に合うものを検索する
		try {
			youtube = new YouTube.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpRequestInitializer() {
			public void initialize(HttpRequest request) throws IOException {}
			}).setApplicationName("youtube-cmdline-search-sample").build();

			YouTube.Search.List search = youtube.search().list("id,snippet");

			//検索の設定
			String apiKey = properties.getProperty("youtube.apikey");
			search.setKey(apiKey);
			search.setPart("snippet");
			search.setChannelId(YTChannnelID);
			search.setOrder("date");
			search.setType("video");
			search.setEventType("completed");
			search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
			//検索数の最大数を設定
			search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

			//検索実行
			SearchListResponse searchResponse = search.execute();

			//検索の結果をリストにまとめる
			searchResultList = searchResponse.getItems();

			//エラーコード
		} catch (GoogleJsonResponseException e) {
			System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
			+ e.getDetails().getMessage());
		} catch (IOException e) {
			System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return searchResultList;
	}

	public String[] oneVideoData(Iterator<SearchResult> iteratorSearchResults) {
		String oneData[] = new String[3];

		//リストの中身がないエラーメッセージ
		if (!iteratorSearchResults.hasNext()) {
			System.out.println(" There aren't any results for your query.");
		}

		//リストの中身がなくなるまでwhileをまわす
		while (iteratorSearchResults.hasNext()) {

			SearchResult singleVideo = iteratorSearchResults.next();
			//動画ID取得
			ResourceId rId = singleVideo.getId();
			if (rId.getKind().equals("youtube#video")) {

				//サムネイル取得
				Thumbnail thumbnail = (Thumbnail) singleVideo.getSnippet().getThumbnails().get("default");
				vid = rId.getVideoId();
				//タイトル取得
				title = singleVideo.getSnippet().getTitle();
				samune = thumbnail.getUrl();

				oneData[0] = vid;
				oneData[1] = title;
				oneData[2] = samune;

			}//if
		}//while
		return oneData;
	}//prettyPrint

}
