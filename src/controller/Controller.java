package controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Scanner;

import model.data_structures.ArregloDinamico;
import model.data_structures.ILista;
import model.logic.Modelo;
import model.logic.YoutubeVideo;
import view.View;

public class Controller {

	/* Instancia del Modelo*/
	private Modelo modelo;
	
	/* Instancia de la Vista*/
	private View view;
	
	/**
	 * Crear la vista y el modelo del proyecto
	 * @param capacidad tamaNo inicial del arreglo
	 * @throws IOException 
	 * @throws ParseException 
	 */
	public Controller () throws ParseException, IOException
	{
		view = new View();
		modelo = new Modelo();
		modelo.cargar();
	}


	public void run() 
	{
		Scanner lector = new Scanner(System.in);
		boolean fin = false;
		String dato = "";
		Object respuesta = null;

		
		while(!fin){
			view.printMenu();
			
			int option = lector.nextInt();
			switch(option){
				case 1:
					view.printMessage("Ingresa 1 para LinearProbing, Ingresa 2 para SeparateChaining");
					String r = lector.next();
				String res;
				try {
					modelo.cargarId();
					res = (r.equals("1"))?modelo.cargarDatosConLinearProbing():modelo.cargarDatosConSeparateChaining(1,"");
					view.printMessage(res);
					view.printMessage("Total categorias:" +modelo.darCategorias().size());
				} catch (IOException | ParseException e) {
					e.printStackTrace();
				}
				
				case 2:
					view.printMessage("Cual requerimiento se quiere probar?");
					r = lector.next();
					if(r.equals("1")){
						view.printMessage("Ingrese: pais, numero, categoria");
						r = lector.next();
						String[] s = r.split(",");
						ArregloDinamico<YoutubeVideo> d = (ArregloDinamico<YoutubeVideo>) modelo.req1(s[0].toLowerCase().trim(), Integer.parseInt(s[1]), s[2].toLowerCase().trim());
						view.imprimirVideoReq1(d, d.size());
					}					
					else if(r.equals("2")){
						try {
							modelo.cargarDatosConSeparateChaining(2,"");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						view.printMessage("Ingrese: pais");
						r = lector.next();
						//String[] s = r.split(",");
						view.printMessage(modelo.req2(r.toLowerCase().trim()));
						
					}
					else if(r.equals("3")){
							try {
								modelo.cargarDatosConSeparateChaining(3, "");
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						view.printMessage("Ingrese: categoria");
						r = lector.next();
						String[] s = r.split(",");
						//ArregloDinamico<YoutubeVideo> d = (ArregloDinamico<YoutubeVideo>) 
						view.printMessage(modelo.req3(r.toLowerCase().trim()));
						//view.imprimirVideoReq1(d, d.size());
					}
					else if(r.equals("4")){
						view.printMessage("Ingrese: tag(Exacto)");
						r = lector.next();
						String[] s = r.split(",");
						ArregloDinamico<YoutubeVideo> d = (ArregloDinamico<YoutubeVideo>) modelo.req4(r.trim());
						view.imprimirVideoReq(d,d.size());
						//view.imprimirVideoReq1(d, d.size());
					}
					break;
				case 3:
					
				case 4:
					view.printMessage("Pruebas de desempeño");
					break;
				case 5: 
					view.printMessage("--------- \n Hasta pronto !! \n---------"); 
					lector.close();
					fin = true;
					break;				
				
				default: 
					view.printMessage("--------- \n Opcion Invalida !! \n---------");
					break;
			}
		}
		}
	}
