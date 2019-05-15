package rest.controller;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

import rest.dao.UserDAO;
import rest.model.User;

@Path("/users")
public class UserService {

	@GET
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<User> getUsers() {
		return UserDAO.getAllUsers();
	}

	// Controle da resposta (status code, mensagem)
	@GET
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public User getUser(@PathParam("id") int id) {
		return UserDAO.getUser(id);
	}

	@GET
	@Path("/search")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public User getUserByName(@QueryParam("username") String username) {
		return UserDAO.getUserByUsername(username);
	}

	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public User addUser(@FormDataParam("image") InputStream uploadedInputStream,
			@FormDataParam("username") String username, @FormDataParam("password") String password, @FormDataParam("email") String email) {

		return UserDAO.addUser(username, password, email, uploadedInputStream);
	}

	@PUT
	@Path("/{id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public User updateUser(@PathParam("id") int id, @FormDataParam("image") InputStream uploadedInputStream,
			@FormDataParam("image") FormDataContentDisposition contentDispositionHeader,
			@FormDataParam("username") String username, @FormDataParam("password") String password, @FormDataParam("email") String email) {
		
		if(contentDispositionHeader.getFileName() == null) {
			return UserDAO.updateUser(id, username, password, email, null);	
		} else {
			return UserDAO.updateUser(id, username, password, email, uploadedInputStream);
		}
	}

	@DELETE
	@Path("/{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public void deleteUser(@PathParam("id") int id) {
		UserDAO.deleteUser(id);
	}
	
	//Session
	@POST
	@Path("/oi")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces("application/json")
    public Response hello(@Context HttpServletRequest req, @FormDataParam("name") String name) {
        HttpSession session = req.getSession(true);
        Object foo = session.getAttribute("foo");
        System.out.println(session.getId());
        
        if (foo != null) {
            System.out.println(foo.toString());
        } else {
            foo = "bar";
            session.setAttribute("foo", foo);
            System.out.println("first");
        }
        
        return Response.status(Status.OK).entity(foo.toString()).build();
    }
}