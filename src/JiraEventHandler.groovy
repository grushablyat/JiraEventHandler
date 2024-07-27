String url

//String ikey = event.issue.getKey()
//long event_type_id = event.getEventTypeId()
//def change = event?.getChangeLog()?.getRelated("ChildChangeItem")?.find { it.field == "status" }
//URL = "renewing-dinosaur-generally.ngrok-free.app?ikey=" + ikey

Map<String, Object> params = new LinkedHashMap<>()
params.put("ikey", Config.ikey)
params.put("status", Config.status)
params.put("reporter", Config.reporter)

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
//        connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));

        connection.setUseCaches(false);
        connection.setDoOutput(true);

//        connection.getOutputStream().write(postDataBytes);

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



