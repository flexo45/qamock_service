import com.temafon.qa.mock.service.scripts.helper.Helper
import groovy.swing.SwingBuilder
import oracle.jdbc.OracleDriver

import javax.swing.JFrame
import javax.swing.JProgressBar
import javax.swing.JTextArea
import javax.swing.JToolBar
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

findFake()

def findFake(){
    SwingBuilder b = new SwingBuilder()

    JProgressBar bar
    JTextArea log

    b.edt {
        frame(title: "finder", defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE, show: true){
            gridLayout(rows: 2, cols: 1)
            bar = progressBar(stringPainted: true, indeterminate: false)

            log = textArea()
        }
    }

    DriverManager.registerDriver(new OracleDriver())

    Map<String, Integer> dubli = new HashMap<>();

    Connection connection

    DriverManager.registerDriver(new OracleDriver())

    File ids = new File("data_req.txt")

    String select_tp = "SELECT ACTION, LEAD_ID, SERVICE_NAME FROM TP_NOTIFICATIONS WHERE REQUEST_ID = '{req_id}'"

    int maxRow = 0
    ids.eachLine {
        maxRow++
    }
    bar.maximum = maxRow
    bar.string = "0"
    bar.value = 0
    int n = 0

    try{
        connection = DriverManager.getConnection("jdbc:oracle:thin:campaigns/campaigns@192.168.37.26:1521:PROD5")

        ids.eachLine {

            n++

            def s1 = connection.createStatement()

            def arr = it.tokenize(",")
            def req_id = arr.get(2)

            ResultSet resultSet = s1.executeQuery(select_tp.replace("{req_id}", req_id))

            sleep(100)

            if(resultSet.next()){
                def action = resultSet.getString("ACTION")
                def lead = resultSet.getString("LEAD_ID")
                def sn = resultSet.getString("SERVICE_NAME")

                if(!action.equals("TARIFFICATION")){
                    log.append("No tariffication request $req_id")
                    System.out.println("No tariffication request $req_id")
                }
                else if(!lead.equals(arr.get(1))){
                    log.append("Lead id not for $req_id")
                    System.out.println("Lead id not for $req_id")
                }

                if(dubli.get(req_id) == null){dubli.put(req_id, 0)}
                else {
                    dubli.put(req_id, ++dubli.get(req_id))
                    log.append("$req_id is dubl': ${dubli.get(req_id)}")
                }
            }
            else {
                log.append("Request $req_id not found")
                System.out.println("Request $req_id not found")
            }

            s1.close();;

            bar.value++
            bar.string = "$n"
        }

        connection.close();
    }
    catch (Exception e){
        connection.close()
        /*
        File v = new File("res.txt")
        result.each {
            v.append("${it.key};${it.value};\r\n")
        }
        */

        throw e
    }
}

def findAllMoney(){

    SwingBuilder b = new SwingBuilder()

    JProgressBar bar
    JTextArea log

    b.edt {
        frame(title: "calculator", defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE, show: true){
            gridLayout(rows: 2, cols: 1)
            bar = progressBar(stringPainted: true, indeterminate: false)

            log = textArea()
        }
    }

    DriverManager.registerDriver(new OracleDriver())

    Map<String, Integer> result = [:]

    Connection connection

    DriverManager.registerDriver(new OracleDriver())

    File ids = new File("data_req.txt")

    String select_subs = "SELECT s.ID, u.MSISDN FROM SUBSCRIPTIONS s, USERS u WHERE s.USER_ID = u.ID AND s.LEAD_ID = '{lead_id}'"

    String select_charges = "SELECT COUNT(*) * 20 AS SUMM FROM CHARGE_MTS WHERE SUBSCRIPTION_ID = {subs_id} AND " +
            "TRUNC(CREATED) >= TO_DATE('01.04.2016', 'dd.mm.yyyy') AND STATUS = 1"

    int maxRow = 0
    ids.eachLine {
        maxRow++
    }
    bar.maximum = maxRow
    bar.string = "0"
    bar.value = 0
    int n = 0

    int all_sum = 0

    try{

        connection = DriverManager.getConnection("jdbc:oracle:thin:streaming_new/user12345@192.168.37.28:1521:DWHSTAGE")

        ids.eachLine {

            n++

            def s1 = connection.createStatement()

            ResultSet resultSet = s1.executeQuery(select_subs.replace("{lead_id}", it))

            sleep(100)

            if(resultSet.next()){

                def id = resultSet.getString("ID")
                def msisdn = resultSet.getString("MSISDN")

                //System.out.println("found: id=$id, msisdn=$msisdn")

                s1.close()

                def s2 = connection.createStatement()

                ResultSet res = s2.executeQuery(select_charges.replace("{subs_id}", id))

                sleep(100)

                Integer sum = 0

                if(res.next()){

                     sum = res.getInt("SUMM")

                }

                s2.close()

                all_sum += sum

                result.put("$msisdn;$it", sum)

                System.out.println("$msisdn;$it;$sum -> $all_sum")

            }

            bar.value++
            bar.string = "$n"

        }

        connection.close()
    }
    catch (Exception e){
        connection.close()

        File v = new File("payments04_all_1.txt")
        result.each {
            v.append("${it.key};${it.value};\r\n")
        }

        throw e
    }

    File v = new File("payments04_all_1.txt")
    result.each {
        v.append("${it.key};${it.value};\r\n")
    }
}

def validate(){

    SwingBuilder b = new SwingBuilder()

    JProgressBar bar
    JTextArea log

    b.edt {
        frame(title: "validator", defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE, show: true){
            gridLayout(rows: 2, cols: 1)
            bar = progressBar(stringPainted: true, indeterminate: false)

            log = textArea()
        }
    }

    List<String> data = []
    List<String> sub_data = []

    String q = "SELECT u.MSISDN, s.LEAD_ID, s.START_DATE, s.END_DATE, s.CREATED, s.SERVICE_NAME, s.TITLE," +
            " s.SUBSCRIPTION_TYPE_ID, s.HEADING_ID, s.STATUS, s.CAMPAIGN_ID, s.PROMO_CHANNEL, s.ORDERING_CHANNEL \n" +
            "  FROM SUBSCRIPTIONS s, USERS u WHERE s.USER_ID = u.ID AND s.LEAD_ID='{lead}'"

    //AND TRUNC(s.CREATED) >= TO_DATE('2016-05-01', 'yyyy-mm-dd')

    DriverManager.registerDriver(new OracleDriver())

    Connection connection

    Statement statement

    try{
        connection = DriverManager.getConnection("jdbc:oracle:thin:streaming_new/user12345@192.168.37.28:1521:DWHSTAGE")

        File file = new File("1result_04_uniq.txt")

        int found = 0
        int not_found = 0
        int out_of_date = 0

        int maxRow = 0
        file.eachLine {
            maxRow++
        }

        bar.maximum = maxRow
        bar.string = "0"

        int n = 0

        file.eachLine {
            n++
            statement = connection.createStatement()
            ResultSet r = statement.executeQuery(q.replace("{lead}", it))

            if(r.next()){
                def c = r.getDate("CREATED");

                String res = "${r.getString("MSISDN")};${r.getString("LEAD_ID")};${r.getString("START_DATE")};${r.getString("END_DATE")};${c};${r.getString("SERVICE_NAME")};${r.getString("TITLE")};" +
                        "${r.getString("SUBSCRIPTION_TYPE_ID")};${r.getString("HEADING_ID")};${r.getString("STATUS")};${r.getString("CAMPAIGN_ID")};${r.getString("PROMO_CHANNEL")};${r.getString("ORDERING_CHANNEL")};"

                //System.out.println(res)

                Date apr = new Date(1459458000000)

                if(c >= apr){
                    data.add(res)
                    found++
                }
                else {
                    sub_data.add(res)
                    out_of_date++
                }
            }
            else {
                not_found++
            }

            log.text = "Found: ${found}\r\nNot found: ${not_found}\r\nOut of date: ${out_of_date}"

            bar.value++
            bar.string = "$n"

            statement.close()

            sleep(100)
        }

        connection.close()
    }
    catch (Exception e){
        connection.close()
        throw e
    }

    File v = new File("valid04.txt")
    data.each {
        v.append(it + "\r\n")
    }

    File o = new File("out_date04.txt")
    sub_data.each {
        o.append(it + "\r\n")
    }

}

def dubli(){
    File result = new File("1result_04.txt")

    Map<String, Integer> uniq = [:]

    result.eachLine {
        int x = uniq.get(it)?:-1
        if(x == -1){
            uniq.put(it, 1)
        }
    }

    File file = new File("1result_04_uniq.txt");
    uniq.each {
        file.append(it.key + "\r\n")
    }
}

def find(){
    File result = new File("1result.txt")
    File source = new File("all_finded.txt")

    Map<String, Integer> res = [:]

    source.eachLine {

        String i_line ->
            result.eachLine {
                if(i_line.equals(it)){
                    res.put(i_line, res.get(i_line)?:0 + 1)
                    //System.out.println("$i_line -> ${res.get(i_line)}")
                }
            }
    }

    File file = new File("result_10.txt");
    res.each {
        file.append(it.key + "\r\n")
    }
}
