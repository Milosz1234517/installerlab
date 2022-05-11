package example;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.crypto.SecretKey;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JList;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main frame = new Main();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	KeystoreManager manager = new KeystoreManager();
	JList<String> list = new JList<>();
	DefaultListModel<String> defaultList;
	JScrollPane scrollPane = new JScrollPane();
	JComboBox<String> comboBox = new JComboBox<>();
	
	public void getKeys() {

	}
	
	public void comboInit() {
		comboBox.addItem("RSA");
		comboBox.addItem("AES");
	}
	
	public void choseAlghoritm(String alg, String message) {
		
	}
	
	public void defaultKeystore() {
		try {
			manager.loadKeystore("keys.jks", "123456");
			getKeys();
		} catch (Exception ex) {
			manager.createKeystore("keys", "123456");
			defaultKeystore();
			getKeys();
		}
	}
	
	public File choseFile(String type, String typePrinted) {
		JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setFileFilter(new FileNameExtensionFilter(typePrinted, type));
		fc.showOpenDialog(contentPane);
		return fc.getSelectedFile();
	}
	
	public void performEncrypt(){
		switch((String) comboBox.getSelectedItem()) {
		case "RSA":
			try {
				RSAPrivateKey pr = (RSAPrivateKey) manager.ks.getKey(JOptionPane.showInputDialog("Enter key name"), JOptionPane.showInputDialog("Enter password").toCharArray());
				
				File file = choseFile("txt", "TXT");
				
				FileInputStream fis = new FileInputStream(file.getAbsolutePath());
				byte[] b = Files.readAllBytes(file.toPath());
				fis.close();
				
				FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
				fos.write(new RSA(pr.getModulus().bitLength()).encrypt(new String(b), pr).getBytes());
				fos.close();
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;
		case "AES":
			try {
				SecretKey sc = (SecretKey) manager.ks.getKey(JOptionPane.showInputDialog("Enter key name"),JOptionPane.showInputDialog("Enter password").toCharArray());
				
				File file = choseFile("txt", "TXT");
				
				FileInputStream fis = new FileInputStream(file.getAbsolutePath());
				byte[] b = Files.readAllBytes(file.toPath());
				fis.close();
				
				FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
				fos.write(new AES().encrypt(new String(b), sc).getBytes());
				fos.close();
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;
		}
	}
	
	public void performDecrypt() {
		switch((String) comboBox.getSelectedItem()) {
		case "RSA":
			try {
				RSAPublicKey pu = (RSAPublicKey) manager.ks.getCertificate(JOptionPane.showInputDialog("Enter key name")).getPublicKey();
				
				File file = choseFile("txt", "TXT");
				
				FileInputStream fis = new FileInputStream(file.getAbsolutePath());
				byte[] b = Files.readAllBytes(file.toPath());
				fis.close();
				
				FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
				fos.write(new RSA(pu.getModulus().bitLength()).decrypt(new String(b), pu).getBytes());
				fos.close();
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;
		case "AES":
			try {
				SecretKey sc = (SecretKey) manager.ks.getKey(JOptionPane.showInputDialog("Enter key name"),JOptionPane.showInputDialog("Enter password").toCharArray());
				
				File file = choseFile("txt", "TXT");
				
				FileInputStream fis = new FileInputStream(file.getAbsolutePath());
				byte[] b = Files.readAllBytes(file.toPath());
				fis.close();
				
				FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
				fos.write(new AES().decrypt(new String(b), sc).getBytes());
				fos.close();
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			break;
		}
	}
	
	public Main() {
		
		comboInit();
		
		//defaultKeystore();
		
		JOptionPane.showMessageDialog(null, "Java version: 9");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 725, 595);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton encryptButton = new JButton("Encrypt Data");
		encryptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performEncrypt();
			}
				
		});
		encryptButton.setBounds(112, 95, 201, 23);
		contentPane.add(encryptButton);
		
		JButton decryptButton = new JButton("Decrypt Data");
		decryptButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				performDecrypt();
			}
		});
		decryptButton.setBounds(112, 163, 201, 23);
		contentPane.add(decryptButton);
		
		JButton loadKeystoreButton = new JButton("Load Keystore");
		loadKeystoreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {					
					manager.loadKeystore(choseFile("jks", "JKS").getAbsolutePath(), JOptionPane.showInputDialog("Enter password"));
					getKeys();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		loadKeystoreButton.setBounds(112, 233, 201, 23);
		contentPane.add(loadKeystoreButton);
		
		JButton createKeystoreButton = new JButton("Create Keystore");
		createKeystoreButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser fc = new JFileChooser();
					fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
					fc.showOpenDialog(contentPane);

					manager.createKeystore(fc.getSelectedFile().getAbsolutePath() + "\\" + JOptionPane.showInputDialog("Enter name"), JOptionPane.showInputDialog("Enter password"));
					getKeys();
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		createKeystoreButton.setBounds(112, 305, 201, 23);
		contentPane.add(createKeystoreButton);
		
		comboBox.setBounds(377, 163, 170, 22);
		contentPane.add(comboBox);
		
		JLabel lblNewLabel = new JLabel("Chose Alghoritm");
		lblNewLabel.setBounds(393, 119, 170, 14);
		contentPane.add(lblNewLabel);
		
		scrollPane.setBounds(362, 216, 201, 232);
		contentPane.add(scrollPane);
		
		scrollPane.setViewportView(list);
	}
}
