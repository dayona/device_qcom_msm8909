package ac.iitm.htic.simplenativedaemon;

import android.app.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MainActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b=(Button)findViewById(R.id.button);
        cpp.bn(true);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Thread t=new Thread(new Runnable(){

                    @Override
                    public void run() {
                        sendonemsg();
                    }
                });
                t.start();
            }
        });
    }
    void sendonemsg()
    {
        DatagramSocket s;
        try {
            s = new DatagramSocket();
            InetAddress local = InetAddress.getByName("127.0.0.1");
            String str="hello service";

            int msg_length = str.length();
            DatagramPacket p = new DatagramPacket(str.getBytes(), msg_length, local, 2000);
            s.send(p);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


}
