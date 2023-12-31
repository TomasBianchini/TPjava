package data;

import java.sql.*;
import java.util.LinkedList;
import entities.Vuelo;
import entities.Aeropuerto;
import entities.Avion;
import entities.Ciudad;
import entities.Pais;
import java.time.*; 
public class DataVuelo {
	
	public LinkedList<Vuelo> getAll(){
		Statement stmt = null; 
		ResultSet rs = null;
		LinkedList<Vuelo> vuelos = new LinkedList<>();
			try {
				stmt= DbConnector.getInstancia().getConn().createStatement();
				rs= stmt.executeQuery("select vue.*, ciuO.nombre as nCiudadO, "
						+ "ciuD.nombre as nCiudadD, aeroO.nombre as nAeroO, aeroD.nombre as nAeroD,"
						+ " pO.nombre as nPaisO, pD.nombre as nPaisD "
						+ " from vuelo vue "
						+ " inner join aeropuerto aeroO on aeroO.idaeropuerto = vue.idAeropuertoOrigen"
						+ " inner join aeropuerto aeroD on aeroD.idaeropuerto = vue.idAeropuertoDestino"
						+ " inner join ciudad ciuO on ciuO.codPostal = aeroO.codPostal "
						+ " inner join ciudad ciuD on ciuD.codPostal = aeroD.codPostal "
						+ " inner join pais pO on pO.idpais = ciuO.idPais"
						+ " inner join pais pD on pD.idpais = ciuD.idPais");
				if(rs!=null) {
					while(rs.next()) {
						Vuelo v = new Vuelo();
						v.setAeropuertoDestino(new Aeropuerto());
						v.setAeropuertoOrigen(new Aeropuerto());
						v.getAeropuertoDestino().setCiudad(new Ciudad());
						v.getAeropuertoOrigen().setCiudad(new Ciudad()); 
						v.getAeropuertoDestino().getCiudad().setPais(new Pais());
						v.getAeropuertoOrigen().getCiudad().setPais(new Pais());
						v.setIdvuelo(rs.getInt("idVuelo"));
						v.setFechaHoraSalida(rs.getObject("fechaHoraSalida",LocalDateTime.class));
						v.setFechaHoraLlegada(rs.getObject("fechaHoraLlegada",LocalDateTime.class));	
						v.getAeropuertoOrigen().setIdAeropuerto(rs.getInt("idAeropuertoOrigen"));
						v.getAeropuertoOrigen().setNombre(rs.getString("nAeroO"));
						v.getAeropuertoDestino().setIdAeropuerto(rs.getInt("idAeropuertoDestino"));
						v.getAeropuertoDestino().setNombre(rs.getString("nAeroD"));
						v.getAeropuertoOrigen().getCiudad().setNombre(rs.getString("nCiudadO"));
						v.getAeropuertoDestino().getCiudad().setNombre(rs.getString("nCiudadD"));
						v.getAeropuertoOrigen().getCiudad().getPais().setNombre(rs.getString("nPaisO"));
						v.getAeropuertoDestino().getCiudad().getPais().setNombre(rs.getString("nPaisD"));
						vuelos.add(v);					
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();	
			} finally {
				try {
					if(rs!=null) {rs.close();}
					if(stmt!=null) {stmt.close();}
					DbConnector.getInstancia().releaseConn();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			return vuelos;
	}
	
	public Vuelo getById(Vuelo v) {
		Vuelo vue = null; 
		PreparedStatement stmt=null;
		ResultSet rs=null;
		try {
			stmt=DbConnector.getInstancia().getConn().prepareStatement(
					"select vue.*, ciuO.nombre as nCiudadO, "
							+ "ciuD.nombre as nCiudadD, aeroO.nombre as nAeroO, aeroD.nombre as nAeroD,"
							+ " pO.nombre as nPaisO, pD.nombre as nPaisD "
							+ " from vuelo vue "
							+ " inner join aeropuerto aeroO on aeroO.idaeropuerto = vue.idAeropuertoOrigen"
							+ " inner join aeropuerto aeroD on aeroD.idaeropuerto = vue.idAeropuertoDestino"
							+ " inner join ciudad ciuO on ciuO.codPostal = aeroO.codPostal "
							+ " inner join ciudad ciuD on ciuD.codPostal = aeroD.codPostal "
							+ " inner join pais pO on pO.idpais = ciuO.idPais"
							+ " inner join pais pD on pD.idpais = ciuD.idPais "
							+ " inner join avion av on av.idavion = vue.idavion "
							+ " where vue.idvuelo = ?");
			
			stmt.setInt(1, v.getIdvuelo());
			rs=stmt.executeQuery();
			if(rs!=null && rs.next()) {
				vue =new Vuelo();
				vue.setAeropuertoDestino(new Aeropuerto());
				vue.setAeropuertoOrigen(new Aeropuerto());
				vue.getAeropuertoDestino().setCiudad(new Ciudad());
				vue.getAeropuertoOrigen().setCiudad(new Ciudad()); 
				vue.getAeropuertoDestino().getCiudad().setPais(new Pais());
				vue.getAeropuertoOrigen().getCiudad().setPais(new Pais());
				vue.setAvion(new Avion()); 
				vue.setIdvuelo(rs.getInt("idVuelo"));
				v.setFechaHoraSalida(rs.getObject("fechaHoraSalida",LocalDateTime.class));
				v.setFechaHoraLlegada(rs.getObject("fechaHoraLlegada",LocalDateTime.class));
				vue.getAeropuertoOrigen().setIdAeropuerto(rs.getInt("idAeropuertoOrigen"));
				vue.getAeropuertoOrigen().setNombre(rs.getString("nAeroO"));
				vue.getAeropuertoDestino().setIdAeropuerto(rs.getInt("idAeropuertoDestino"));
				vue.getAeropuertoDestino().setNombre(rs.getString("nAeroD"));
				vue.getAeropuertoOrigen().getCiudad().setNombre(rs.getString("nCiudadO"));
				vue.getAeropuertoDestino().getCiudad().setNombre(rs.getString("nCiudadD"));
				vue.getAeropuertoOrigen().getCiudad().getPais().setNombre(rs.getString("nPaisO"));
				vue.getAeropuertoDestino().getCiudad().getPais().setNombre(rs.getString("nPaisD"));
				vue.getAvion().setIdAvion(rs.getInt("idAvion"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) {rs.close();}
				if(stmt!=null) {stmt.close();}
				DbConnector.getInstancia().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return vue;
	}
	
	public LinkedList<Vuelo> getByOrigenYDestino(Vuelo v) {
		Vuelo vue = null; 
		PreparedStatement stmt=null;
		ResultSet rs=null;
		LinkedList<Vuelo> vuelos = new LinkedList<>();
		try {
			stmt=DbConnector.getInstancia().getConn().prepareStatement(
					"select vue.*, ciuO.nombre as nCiudadO, "
							+ "ciuD.nombre as nCiudadD, aeroO.nombre as nAeroO, aeroD.nombre as nAeroD,"
							+ " pO.nombre as nPaisO, pD.nombre as nPaisD "
							+ " from vuelo vue "
							+ " inner join aeropuerto aeroO on aeroO.idaeropuerto = vue.idAeropuertoOrigen"
							+ " inner join aeropuerto aeroD on aeroD.idaeropuerto = vue.idAeropuertoDestino"
							+ " inner join ciudad ciuO on ciuO.codPostal = aeroO.codPostal "
							+ " inner join ciudad ciuD on ciuD.codPostal = aeroD.codPostal "
							+ " inner join pais pO on pO.idpais = ciuO.idPais"
							+ " inner join pais pD on pD.idpais = ciuD.idPais"
							+ " where vue.idAeropuertoOrigen = ? and vue.idAeropuertoDestino = ?");
			
			stmt.setInt(1, v.getAeropuertoOrigen().getIdAeropuerto());
			stmt.setInt(2, v.getAeropuertoDestino().getIdAeropuerto()); 
			rs=stmt.executeQuery();
			if(rs!=null && rs.next()) {
				vue =new Vuelo();
				vue.setAeropuertoDestino(new Aeropuerto());
				vue.setAeropuertoOrigen(new Aeropuerto());
				vue.getAeropuertoDestino().setCiudad(new Ciudad());
				vue.getAeropuertoOrigen().setCiudad(new Ciudad()); 
				vue.getAeropuertoDestino().getCiudad().setPais(new Pais());
				vue.getAeropuertoOrigen().getCiudad().setPais(new Pais());
				vue.setIdvuelo(rs.getInt("idVuelo"));
				v.setFechaHoraSalida(rs.getObject("fechaHoraSalida",LocalDateTime.class));
				v.setFechaHoraLlegada(rs.getObject("fechaHoraLlegada",LocalDateTime.class));
				vue.getAeropuertoOrigen().setIdAeropuerto(rs.getInt("idAeropuertoOrigen"));
				vue.getAeropuertoOrigen().setNombre(rs.getString("nAeroO"));
				vue.getAeropuertoDestino().setIdAeropuerto(rs.getInt("idAeropuertoDestino"));
				vue.getAeropuertoDestino().setNombre(rs.getString("nAeroD"));
				vue.getAeropuertoOrigen().getCiudad().setNombre(rs.getString("nCiudadO"));
				vue.getAeropuertoDestino().getCiudad().setNombre(rs.getString("nCiudadD"));
				vue.getAeropuertoOrigen().getCiudad().getPais().setNombre(rs.getString("nPaisO"));
				vue.getAeropuertoDestino().getCiudad().getPais().setNombre(rs.getString("nPaisD"));
				vuelos.add(vue);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) {rs.close();}
				if(stmt!=null) {stmt.close();}
				DbConnector.getInstancia().releaseConn();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return vuelos;
	}

	
	public void add(Vuelo v) {
		PreparedStatement stmt= null;
		try {
			stmt=DbConnector.getInstancia().getConn().
					prepareStatement(
							"insert into vuelo(idvuelo, fechaHoraSalida, fechaHoraLlegada, "
							+ " idAeropuertoOrigen, idAeropuertoDestino, idAvion) values(?,?,?,?,?,?)");
			stmt.setInt(1, v.getIdvuelo());
			stmt.setObject(2, v.getFechaHoraSalida());
			stmt.setObject(3, v.getFechaHoraLlegada());
			stmt.setInt(4, v.getAeropuertoOrigen().getIdAeropuerto());
			stmt.setInt(5, v.getAeropuertoDestino().getIdAeropuerto());
			stmt.setInt(6, v.getAvion().getIdAvion());
			stmt.executeUpdate();
		}  catch (SQLException e) {
            e.printStackTrace();
		} finally {
            try {
                if(stmt!=null)stmt.close();
                DbConnector.getInstancia().releaseConn();
            } catch (SQLException e) {
            	e.printStackTrace();
            }
		}	
	}
	
	public void delete(Vuelo v) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement(
					"delete from vuelo where idvuelo=?");
			pstmt.setInt(1, v.getIdvuelo());
			pstmt.executeUpdate();
		}catch(SQLException e){
			e.printStackTrace(); 
		}finally{
	            try {
	                if(pstmt!=null)pstmt.close();
	                DbConnector.getInstancia().releaseConn();
	            } catch (SQLException e) {
	            	e.printStackTrace();
	            }
		}
	}
		
	public void edit(Vuelo v) {
		PreparedStatement pstmt = null;
		try {
			pstmt = DbConnector.getInstancia().getConn().prepareStatement
					("UPDATE vuelo SET fechaHoraSalida=?, fechaHoraLlegada=?, idAvion = ? WHERE idvuelo=?");
			pstmt.setInt(4, v.getIdvuelo());
			pstmt.setObject(1, v.getFechaHoraSalida());
			pstmt.setObject(2, v.getFechaHoraLlegada());
			pstmt.setInt(3, v.getAvion().getIdAvion());
			pstmt.executeUpdate();	
		}  catch (SQLException e) {
            e.printStackTrace();
		} finally {
            try {
                if(pstmt!=null)pstmt.close();
                DbConnector.getInstancia().releaseConn();
            } catch (SQLException e) {
            	e.printStackTrace();
            }
		}
	}
}
