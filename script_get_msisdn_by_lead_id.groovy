import groovy.sql.Sql
import groovy.swing.SwingBuilder
import oracle.jdbc.OracleDriver

import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JProgressBar
import javax.swing.JTextArea
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

findFake()

def findFake(){
    SwingBuilder b = new SwingBuilder()
    JProgressBar bar
    JTextArea log
    JButton but

    boolean stopped = false;

    List<String[]> result = []

    b.edt {
        frame(title: "finder", defaultCloseOperation: JFrame.DISPOSE_ON_CLOSE, windowClosed: {stop(stopped)}, show: true){
            gridLayout(rows: 2, cols: 1)
            bar = progressBar(stringPainted: true, indeterminate: false)

            //log = textArea()

            but = button(text: "Report", actionPerformed: {saveRep(result)})
        }
    }

    File leads = new File("big_data.csv")

    DriverManager.registerDriver(new OracleDriver())

    Connection connection

    String select_subs = "SELECT s.START_DATE, u.MSISDN, s.LEAD_ID, s.SERVICE_NAME, s.STATUS, s.CAMPAIGN_ID" +
            " FROM SUBSCRIPTIONS s, USERS u" +
            " WHERE s.USER_ID = u.ID AND s.CREATED >= SYSDATE - 120 AND s.LEAD_ID = '{lead}'"

    int maxRow = 0
    leads.eachLine {
        maxRow++
    }
    bar.maximum = maxRow
    bar.string = "0"
    bar.value = 0
    int n = 0

    System.out.println("UI initialized")

    System.out.println("SQL created")

    connection = DriverManager.getConnection("jdbc:oracle:thin:streaming_new/user12345@192.168.37.35:1521:PROD5")

    try{

        def s1 = connection.createStatement()

        s1.setQueryTimeout(30)

        leads.eachLine {

            n++

            def arr = it.tokenize(";")
            def lead = arr.get(0)

            ResultSet resultSet = s1.executeQuery(select_subs.replace("{lead}", lead))

            sleep(50)

            if(resultSet.next()){
                System.out.println("Subscription for $lead found")

                def start_d = resultSet.getString("START_DATE")
                def msisdn = resultSet.getString("MSISDN")
                def lead_id = resultSet.getString("LEAD_ID")
                def service_name = resultSet.getString("SERVICE_NAME")
                def status = resultSet.getString("STATUS")
                def campaign = resultSet.getString("CAMPAIGN_ID")

                resultSet.close();

                String[] row = [start_d, msisdn, lead_id, service_name, status, campaign]

                result.add(row)
            }

            //s1.close();

            bar.value++
            bar.string = "$n"
        }

        s1.close();

        connection.close();


    }
    catch (Exception e){
        System.out.println(e)
    }
    finally {
        connection.close()
        s1.close();
        //sql.close()

        saveRep(result)

    }
}

def saveRep(List<String[]> result){
    System.out.println("Load result to file")
    File v = new File("big_report.txt")
    v.append("start_date;msisdn;lead_id;service_name;status;campaign_id\r\n")
    result.each {
        v.append("${it[0]};${it[1]};${it[2]};${it[3]};${it[4]};${it[5]}\r\n")
    }
    System.out.println("Load result to file ... complete")
}

def stop(flag){
    flag = true;
}
