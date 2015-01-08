package webservice;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import model.GCM;

@Path("/gcmservice")
public class GCMService {
	@POST
	@Path("/RegisterGCM")
	@Produces("application/json;charset=utf-8")
	public String registerGCM(
			@FormParam("regId") String regID) {
		String json = "";
		int status = new GCM(regID).registerGCM();
		if (status != 0) {
			json = "{\"regId\":" + regID + "}";
		}
		return json;
	}
	
	@POST
	@Path("/UnRegisterGCM")
	@Produces("application/json;charset=utf-8")
	public String unRegisterGCM(
			@FormParam("regId") String regID) {
		String json = "";
		int status = new GCM(regID).unRegisterGCM();
		
		return json;
	}
	
	@POST
	@Path("/UpdateGCM")
	@Produces("application/json;charset=utf-8")
	public String updateGCM(
			@FormParam("oldRegId") String oldRegID,
			@FormParam("newRegId") String newregID) {
		String json = "";
		int status = new GCM(oldRegID).updateGCM(newregID);
		return json;
	}
}
