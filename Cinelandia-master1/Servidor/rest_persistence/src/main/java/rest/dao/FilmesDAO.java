package rest.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import rest.model.Filmes;
import rest.util.DbUtil;

public class FilmesDAO {

	private static Connection connection = DbUtil.getConnection();

	public static Filmes addFilmes(String nomefilme, String sinopse, InputStream input) {
		try {
			PreparedStatement pStmt = connection.prepareStatement("insert into Filmes(nomefilme, sinopse) values (?, ?)",
					Statement.RETURN_GENERATED_KEYS);
			pStmt.setString(1, nomefilme);
			pStmt.setString(2, sinopse);
			pStmt.executeUpdate();
			ResultSet rs = pStmt.getGeneratedKeys();
			if (rs.next()) {
				uploadFile(input, rs.getInt("id"));
				return new Filmes(rs.getInt("id"), rs.getString("nomefilme"), rs.getString("sinopse"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Filmes updateFilmes(int id, String nomefilme, String sinopse, InputStream input) {
		try {
			PreparedStatement pStmt = connection.prepareStatement("update Filmes set nomefilme=?, sinopse=? where id=?",
					Statement.RETURN_GENERATED_KEYS);
			pStmt.setString(1, nomefilme);
			pStmt.setString(2, sinopse);
			pStmt.setInt(3, id);
			pStmt.executeUpdate();
			ResultSet rs = pStmt.getGeneratedKeys();
			if (rs.next()) {
				if(input != null)
					uploadFile(input, rs.getInt("id"));
				return new Filmes(rs.getInt("id"), rs.getString("nomefilme"), rs.getString("sinopse"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void deleteFilmes(int id) {
		try {
			PreparedStatement pStmt = connection.prepareStatement("delete from filmes where id=?");
			pStmt.setInt(1, id);
			pStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static List<Filmes> getAllFilmes() {
		List<Filmes> filmes = new ArrayList<Filmes>();
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("select * from filmes order by id");
			while (rs.next()) {
				Filmes filme = new Filmes(rs.getInt("id"), rs.getString("nomefilme"), rs.getString("sinopse"));
				filmes.add(filme);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return filmes;
	}

	public static Filmes getFilmes(int id) {
		try {
			PreparedStatement pStmt = connection.prepareStatement("select * from filmes where id=?");
			pStmt.setInt(1, id);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				return new Filmes(rs.getInt("id"), rs.getString("nomefilme"), rs.getString("sinopse"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Filmes getFilmesByNomefilme(String nomefilme) {
		try {
			PreparedStatement pStmt = connection.prepareStatement("select * from filmes where nomefilme=?");
			pStmt.setString(1, nomefilme);
			ResultSet rs = pStmt.executeQuery();
			if (rs.next()) {
				return new Filmes(rs.getInt("id"), rs.getString("nomefilme"), rs.getString("sinopse"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static void uploadFile(InputStream uploadedInputStream, int id) {
		try {
			InputStream inputStream = DbUtil.class.getClassLoader().getResourceAsStream("uploads.properties");
			Properties prop = new Properties();
			prop.load(inputStream);
			String folder = prop.getProperty("folder");
			String filePath = folder + id;
			saveFile(uploadedInputStream, filePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void saveFile(InputStream uploadedInputStream, String serverLocation) {

		try {
			OutputStream outpuStream = new FileOutputStream(new File(serverLocation));
			int read = 0;
			byte[] bytes = new byte[1024];

			outpuStream = new FileOutputStream(new File(serverLocation));
			while ((read = uploadedInputStream.read(bytes)) != -1) {
				outpuStream.write(bytes, 0, read);
			}
			outpuStream.flush();
			outpuStream.close();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
