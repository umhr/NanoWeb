package jp.mztm.nanoweb;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * 参考
 * IPアドレス取得メソッド
 * https://kaede.jp/2019/05/01223526/
 */
public class IPAddress {
    public String getIPAddress(){
        String result = "";
        try{
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            if(interfaces == null){
                return "IPアドレス取得できませんでした。" + System.lineSeparator();
            }
            while(interfaces.hasMoreElements()){
                NetworkInterface network = interfaces.nextElement();
                Enumeration<InetAddress> addresses = network.getInetAddresses();
                while (addresses.hasMoreElements()){
                    InetAddress address = addresses.nextElement();
                    if(address.getHostAddress().indexOf(".") > -1 && address.getHostAddress().indexOf("127.0.0.1") == -1){
                        result += address.getHostAddress().trim();// + System.lineSeparator();
                    }
                }
            }

        }catch (SocketException e){
            e.printStackTrace();
        }
        return result;
    }
}
