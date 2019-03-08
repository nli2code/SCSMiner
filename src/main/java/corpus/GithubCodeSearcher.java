package corpus;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.fluent.Request;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Config;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

public class GithubCodeSearcher {

  private int resultPageRange = 10;
  private String githubAccessToken = "";
  private String resultLocalPath = "";
  private String projectPackageName = "";
  private String query = "";
  private final int snippetMaxNum = 1000; // the GitHub Search API provides up to 1,000 results for each search.

  public enum RETURN_MODE {
    CONTENT, URL
  }

  public GithubCodeSearcher(String token, String storePath, String filterPackage, String query) {
    githubAccessToken = token;
    resultLocalPath = storePath;
    projectPackageName = filterPackage;
    this.query = query;
  }

  public void run(){
    List<String> resultList = search(query, RETURN_MODE.CONTENT);
    int cnt = 0;
    for (int i = 0; i < resultList.size(); i++) {
      if (cnt >= snippetMaxNum) break;
      if (!resultList.get(i).contains(projectPackageName)) continue;
      File file=new File(
              resultLocalPath + "/" + cnt + ".java");
      try {
        FileUtils.writeStringToFile(file, resultList.get(i));
        cnt++;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  public List<String> search(String query, RETURN_MODE mode){
    List<String> keywords = new ArrayList<String>() {{ add(query); }};
    List<String> r = new ArrayList<>();
    for (int i = 0; i <= resultPageRange ;i++) {
      try {
        Thread.sleep(10000);
        r.addAll(crawl(keywords, i, mode));
      } catch (InterruptedException | IOException e) {
        e.printStackTrace();
      }
    }
    return r;
  }

  public List<String> crawl(List<String> keywords, int pageNum, RETURN_MODE mode) throws IOException {
    String url = "https://api.github.com/search/code?page=" + pageNum +
            "&per_page=500&access_token=" + githubAccessToken +
            "&q=language:Java+" + String.join("+", keywords);
    return searchByUrl(url,mode);
  }

  public List<String> searchByUrl(String url, RETURN_MODE mode) throws IOException {
    List<String> r=new ArrayList<>();
    System.out.println(url);
    String str=Request.Get(url).connectTimeout(10000).socketTimeout(10000)
            .execute().returnContent().asString();
    if (str==null) {
      return new ArrayList<>();
    }

    ExecutorService pool = Executors.newFixedThreadPool(100);
    List<Future> futureList=new ArrayList<>();

    JSONObject jsonObject = new JSONObject(str);
    JSONArray items=jsonObject.getJSONArray("items");
    Iterator<Object> itemIterator=items.iterator();
    while (itemIterator.hasNext()){
      Object itemObject=itemIterator.next();
      if (!(itemObject instanceof JSONObject))
        continue;
      JSONObject item=(JSONObject)itemObject;
      String itemUrlStr=item.getString("html_url");
      itemUrlStr=itemUrlStr.replace("https://github.com/","https://raw.githubusercontent.com/");
      itemUrlStr=itemUrlStr.replace("/blob/","/");
      if (mode==RETURN_MODE.URL)
        r.add(itemUrlStr);
      else {
        Future f = pool.submit(new HtmlCrawler(itemUrlStr));
        futureList.add(f);
      }
    }
    pool.shutdown();
    for (Future f:futureList)
      try {
        if (f.get() == null) continue;
        r.add(f.get().toString());
      } catch (InterruptedException|ExecutionException e) {
        e.printStackTrace();
      }

    return r;
  }

  class HtmlCrawler implements Callable<String> {

    private final String url;

    HtmlCrawler(String url) {
      this.url = url;
    }

    @Override
    public String call() {
      try {
        return Request.Get(url)
                .connectTimeout(10000)
                .socketTimeout(10000)
                .execute().returnContent().asString();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return null;
    }
  }

  public static void main(String[] args) {
    GithubCodeSearcher searcher = new GithubCodeSearcher(Config.getGithubAccessToken(),
            Config.getFileCorpusPath(), Config.getFilterPackage(), Config.getSearchQuery());
    searcher.run();
  }

}
