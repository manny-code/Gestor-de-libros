package view.GUI;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;

import controller.Gestor;
import model.LibroModel;
import view.View;
import view.GUI.GUILibroView.OperacionLibro;

public class GUIMainView extends JFrame implements View {

	private static final long serialVersionUID = 3877840112281408794L;
	
	private Component parentComponent;
	private Rectangle defaultBounds;
	private JPanel contentPane;
	
	private Gestor gestor;
	
	public GUIMainView(Component parentComponent) {
		super();
		setTitle("Gestor de Libros");
		this.parentComponent = parentComponent;
		this.defaultBounds = this.parentComponent.getBounds();
		setBounds(this.defaultBounds);
		setLocationRelativeTo(null);
		setResizable(false);
		setUndecorated(true);
		getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				actualizarDb();
			}
		});
		
		this.contentPane = new JPanel();
		this.contentPane.setLayout(null);
		setContentPane(this.contentPane);
		
		JButton btnAlta = new JButton("Alta");
		btnAlta.setBounds(35, 40, 120, 25);
		btnAlta.setToolTipText("Dar de alta un nuevo libro.");
		btnAlta.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirDetalleLibro(new LibroModel(), OperacionLibro.Alta);
			}
		});
		contentPane.add(btnAlta);
		
		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.setBounds(175, 40, 120, 25);
		btnActualizar.setToolTipText("Actualizaci\u00f3n de la informaci\u00f3n de un libro ingresando el ISBN.");
		btnActualizar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LibroModel libro = obtenerLibro();
				if(libro!=null){
					abrirDetalleLibro(libro, OperacionLibro.Actualizar);
				} else {
					mostrarLibroNoEncontrado();
				}
			}
		});
		contentPane.add(btnActualizar);
		
		JButton btnBaja = new JButton("Baja");
		btnBaja.setBounds(315, 40, 120, 25);
		btnBaja.setToolTipText("Eliminar un libro ingresando el ISBN.");
		btnBaja.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LibroModel libro = obtenerLibro();
				if(libro!=null){
					int option = JOptionPane.showConfirmDialog(
							null, 
							String.format(
								"�Seguro quieres borrar el libro con ISBN %s?",
								libro.getISBN()
							),
							"�Atenci\u00f3n!", 
					    	JOptionPane.YES_NO_OPTION
					   );
					if(option==0){
						gestor.getBibliotecaModel().eliminarLibro(libro);
						JOptionPane.showMessageDialog(null, "�Libro borrado!");
					}
				} else {
					mostrarLibroNoEncontrado();
				}
			}
		});
		contentPane.add(btnBaja);
		
		JButton btnConsulta = new JButton("Consultar");
		btnConsulta.setBounds(35, 115, 120, 25);
		btnConsulta.setToolTipText("Consultar la informaci\u00f3n de un libro ingresando el ISBN.");
		btnConsulta.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LibroModel libro = obtenerLibro();
				if(libro!=null){
					abrirDetalleLibro(libro, OperacionLibro.Consulta);
				} else {
					mostrarLibroNoEncontrado();
				}
			}
		});
		contentPane.add(btnConsulta);
		
		JButton btnListar = new JButton("Listar");
		btnListar.setBounds(175, 115, 120, 25);
		btnListar.setToolTipText("Listar toda la informaci\u00f3n de los libros almacenados.");
		btnListar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				abrirListarLibros();
			}
		});
		contentPane.add(btnListar);
		
		JButton btnOrdenar = new JButton("Ordenar");
		btnOrdenar.setBounds(315, 115, 120, 25);
		btnOrdenar.setToolTipText("Ordenar los libros respecto al ISBN.");
		btnOrdenar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gestor.getBibliotecaModel().ordenarLibros();
				JOptionPane.showMessageDialog(null, "Registros ordenados.");
			}
		});
		contentPane.add(btnOrdenar);
		
		JButton btnSalir = new JButton("Salir");
		btnSalir.setBounds(315, 230, 120, 25);
		btnSalir.setToolTipText("Salir de la aplicacin guardando los datos de los libros.");
		btnSalir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				int option = JOptionPane.showConfirmDialog(
						null, "�Seguro quieres salir?", "�Atenci\u00f3n!", 
				    	JOptionPane.YES_NO_OPTION
				   );
				
				if(option==0){
					actualizarDb();
					System.exit(0);
				}
			}
		});
		contentPane.add(btnSalir);
	}
	
	private void actualizarDb() {
		this.gestor.getBibliotecaModel().actualizarBaseDeDatos();
		this.gestor.getUsuariosModel().actualizarBaseDeDatos();
	}

	private LibroModel obtenerLibro() {
		String ISBN = (String) JOptionPane.showInputDialog(
                this, "Ingresar ISBN", "");
		
		if (ISBN == null)
			return null;
			
		LibroModel libroAux = new LibroModel();
		libroAux.setISBN(ISBN);
		return this.gestor.getBibliotecaModel().getLibro(libroAux);
	}
	
	private void mostrarLibroNoEncontrado(){
		JOptionPane.showMessageDialog( 
				this, "�ISBN no encontrado!", "Error", JOptionPane.ERROR_MESSAGE
			);
	}
	
	private void abrirDetalleLibro(LibroModel libro, OperacionLibro opearcion) {
		GUILibroView libroView = new GUILibroView(this);
		libroView.setGestor(this.gestor);
		libroView.setOperacion(opearcion);
		libroView.setLibro(libro);
		libroView.mostrarView();
	}

	private void abrirListarLibros() {
		GUIListaLibrosView listaLibrosView = new GUIListaLibrosView(this);
		listaLibrosView.setGestor(this.gestor);
		listaLibrosView.mostrarView();
	}
	
	@Override
	public void mostrarView() {
		this.setVisible(true);
		Window parentWindow = (Window) this.parentComponent;
		parentWindow.dispose();
	}
	
	@Override
	public void setGestor(Gestor gestor) {
		this.gestor = gestor;
	}

	public Rectangle getDefaultBounds() {
		return defaultBounds;
	}

	public Component getParentComponent() {
		return parentComponent;
	}
	
}
