package cazaDelMonstruo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

class ServidorTCP {
	private Socket socketCliente;
	private ServerSocket socketServidor;
	private BufferedReader entrada;
	private PrintWriter salida;

	public ServidorTCP(int puerto) {
		this.socketCliente = null;
		this.socketServidor = null;
		this.entrada = null;
		this.salida = null;
		try {
			socketServidor = new ServerSocket(puerto);
			System.out.println("Esperando conexi�n...");
			socketCliente = socketServidor.accept();
			System.out.println("Conexi�n acceptada: " + socketCliente);
			entrada = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
			salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socketCliente.getOutputStream())), true);
		} catch (IOException e) {
			System.out.println("No puede escuchar en el puerto: " + puerto);
			System.exit(-1);
		}
	}

	public void closeServidorTCP() {
		try {
			salida.close();
			entrada.close();
			socketCliente.close();
			socketServidor.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("-> Servidor Terminado");
	}

	public String recibirMsg() {
		String linea = "";
		try {
			linea = entrada.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return linea;
	}

	public int recibirNum() {
		return Integer.parseInt(recibirMsg());
	}

	public void enviarMsg(String linea) {
		salida.println(linea);
	}

	public void enviarNum(int numero) {
		salida.println(numero);
	}
}

public class EcoServidor {

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
//		//para generar de manera aleatoria uso un random
		Random rand = new Random();

		System.out.println("Introduce una cordenada en el tablero 10x10: ");
		System.out.println("NO puedes poner algo fuera de ahi");
		System.out.println("Teneis que buscar al monstruo oculto");
		System.out.println("Gana quien mate al monstruo ");
		int fila1 = rand.nextInt(10);
		int columna1 = rand.nextInt(10);
		System.out.println("FANTAMA GENERADO");

		ServidorTCP canal = new ServidorTCP(5555);

		boolean partida = true;
		while (partida) {
			System.out.println("Rival jugando");
			int fila2 = canal.recibirNum();
			int columna2 = canal.recibirNum();

			if (fila2 == fila1 && columna2 == columna1) {
				canal.enviarMsg("¡ACIERTO!");
				System.out.println("Han cazado a tu monstruo en el tablero");
			} else {
				canal.enviarMsg("NADA ENCONTRADO");
			}
			System.out.println("Te toca ahora DIME LA FILA SOLO: ");
			int intentoFila = sc.nextInt();
			canal.enviarNum(intentoFila);
			System.out.println("Te toca ahora DIME LA COLUMNA:");
			int intentoColumna = sc.nextInt();
			canal.enviarNum(intentoColumna);
			String linea = canal.recibirMsg();
			System.out.println(linea);

			
		}
		canal.closeServidorTCP();
	}
}