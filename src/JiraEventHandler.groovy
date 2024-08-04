import com.atlassian.jira.issue.Issue

String url
Map<String, Object> params = new LinkedHashMap<>()

String ikey = event.issue.getKey()
params.put("ikey", ikey)

url = Config.url + Config.action
String result = executePost(url, params)

static String executePost(String targetURL, Map<String, Object> params) {

    StringBuilder postData = new StringBuilder("?")
    for (Map.Entry<String,Object> param : params.entrySet()) {
        if (postData.length() > 1) {
            postData.append("&")
        }
        postData.append(param.getKey() + "=" + param.getValue())
    }

    HttpURLConnection connection = null;

    try {
        URL url = new URL(targetURL + postData);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        connection.setUseCaches(false);
        connection.setDoOutput(true);

        InputStream is = connection.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        return response.toString();
    } catch (Exception e) {
        e.printStackTrace();
        return null;
    } finally {
        if (connection != null) {
            connection.disconnect();
        }
    }
}



