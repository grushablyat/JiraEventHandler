import com.atlassian.jira.issue.Issue
import com.atlassian.jira.component.ComponentAccessor

String url
Map<String, Object> params = new LinkedHashMap<>()

def issue=event.getIssue()

def changeItems = ComponentAccessor.getChangeHistoryManager().getAllChangeItems(issue)
def length = changeItems.size()

if(changeItems[length-1].getProperties()["field"] == "status"){
    String ikey = event.issue.getKey()
    params.put("ikey", ikey)

    // String status = convertToString(changeItems[length-1].getTos())
    // params.put("status", status)

    // params.put("status", changeItems[length-1].getProperties().status)

    url = "https://renewing-dinosaur-generally.ngrok-free.app/transition_made"
    String result = executePost(url, params)
}

// // def change = event?.getChangeLog()?.getRelated("ChildChangeItem")?.find { it.field == "status" }

// // Issue issue = event.getIssue()
// String ikey = event.issue.getKey()
// // String tostring = event.toString()
// // long event_type_id = event.getEventTypeId()

// params.put("ikey", ikey)
// // params.put("status" actionName)
// // params.put("tostring", event.toString())
// // params.put("status", issue.getStatus().name)
// // params.put("reporter", issue.reporter.name)

// // StatusEditedEvent statusEditedEvent = (StatusEditedEvent)event

// // String status = statusEditedEvent.getOriginalStatus().name

// // params.put("status", status)

// url = "https://renewing-dinosaur-generally.ngrok-free.app/transition_made"

// String result = executePost(url, params)

static String convertToString(Map<?, ?> map) {
    if (map.isEmpty) {
        return "NONE"
    }

    StringBuilder mapAsString = new StringBuilder("{");
    for (var key : map.keySet()) {
        mapAsString.append("" + key + "=" + map.get(key) + ", ");
    }
    mapAsString.delete(mapAsString.length()-2, mapAsString.length()).append("}");
    return mapAsString.toString();
}

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



