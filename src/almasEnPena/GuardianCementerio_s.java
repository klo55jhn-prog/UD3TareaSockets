package almasEnPena;

import java.net.*;
import java.util.Scanner;
import java.io.*;

class ServidorUDP {
	private DatagramSocket socketUDP;
	private DatagramPacket recibido;

	public ServidorUDP(int puerto) {
		try {
			this.socketUDP = new DatagramSocket(puerto);
			this.recibido = null;
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public String recibirMsg() {
		try {
			byte[] buffer = new byte[1000];
			DatagramPacket paquete = new DatagramPacket(buffer, buffer.length);
			recibido = new DatagramPacket(buffer, buffer.length);
			socketUDP.receive(paquete);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new String(recibido.getData()).trim();
	}

	public void enviarMsg(String msg) {

		try {
			DatagramPacket respuesta = new DatagramPacket(msg.getBytes(), msg.length(), recibido.getAddress(),
					recibido.getPort());
			socketUDP.send(respuesta);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void closeServidorUDP() {
		socketUDP.close();
		System.out.println("El velo entre mundos se cierra...");	
	}

}

public class GuardianCementerio_s {
	
	
	public static void main(String[] args) {
		int puerto = 5555;
		ServidorUDP canal = new ServidorUDP(puerto);
		Scanner in = new Scanner(System.in);
		String 	nombre = canal.recibirMsg();
		System.out.println("Guardian del cementerio... " + nombre + " Puerto:" + puerto);
		String linea;
		do {
			linea = canal.recibirMsg();
			System.out.println("Cliente: " + linea);
			canal.enviarMsg("Eco - " + linea);
		} while (!linea.equals("FIN DEL HECHIZO!"));
		canal.closeServidorUDP();
	}
}