package cazaDelMonstruo;

import java.net.*;
import java.util.Scanner;
import java.io.*;

class ClienteTCP {
	private Socket socketCliente = null;
	private BufferedReader entrada = null;
	private PrintWriter salida = null;

	public ClienteTCP(String ip, int puerto) {
		try {
			socketCliente = new Socket(ip, puerto);
			System.out.println("Conexión establecida: " + socketCliente);
			entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
			salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);
		} catch (IOException e) {
			System.err.printf("Imposible conectar con ip:%s / puerto:%d",ip,puerto);
			System.exit(-1);
		}
	}
	public int recibirNum() {
		return Integer.parseInt(recibirMsg());
	}
	public void enviarNum(int numero) {
		salida.println(numero);
	}
	public void closeClienteTCP() {
		try {
			salida.close();
			entrada.close();
			socketCliente.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("-> Cliente Terminado");
	}

	public void enviarMsg(String linea) {
		salida.println(linea);
	}

	public String recibirMsg() {
		String msg = "";
		try {
			msg = entrada.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}
}

public class EcoCliente {
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		
		ClienteTCP canal = new ClienteTCP("localhost", 5555);
		
		boolean partida = true;
		while(partida) {
			
			System.out.println("Te toca ahora DIME LA FILA SOLO");
			int intentoFila = sc.nextInt();
			canal.enviarNum(intentoFila);
			System.out.println("Te toca ahora DIME LA COLUMNA SOLO");
			int intentoColumna = sc.nextInt();
			canal.enviarNum(intentoColumna);
		
			String linea = canal.recibirMsg();
			System.out.println(linea);
			if (linea.equals("¡ACIERTO!")) {
				System.out.println("HAS DERROTADO AL MONSTRUO");
				partida = false;
			} else {
				System.out.println("HAS FALLADO");
			}
		}
		canal.closeClienteTCP();
	}		
}