package favodelix;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TopScrController{

	@Autowired
	private DataDao datadao;

	DataBaseExe DBE = new DataBaseExe();
	Data data = new Data();
	YoutubeApp youtubeapp = new YoutubeApp();

	public String[] vidList;
	public String[] titleList;
	public String[] samuneList;

	@RequestMapping(value = "/topScr1", method = RequestMethod.GET)
	public String top(Model model) {
		int i = 0;
		int youso = datadao.count();
		String allData[][] = new String[youso][];
		List<Data> selectData = datadao.select();

		for(Data a: selectData) {
			String channelID = a.getChannelid();
			allData[i] =  DBE.selectYTDT(channelID);
			i++;
		}


		model.addAttribute("youtubeDB",allData);

		return "/favodeli/TopScr";
	}

	@RequestMapping(value = "/Menu", method = RequestMethod.GET)
	public String MainMenu(Model model) {
		model.addAttribute("datalist",datadao.select());
		return "/favodeli/Menu";
	}


	@RequestMapping(value = "/Menu2", method = RequestMethod.GET)
		public String YoutubeMainMenu(Model model){
		model.addAttribute( new Data());
			return "/favodeli/Menu2";
		}

	@RequestMapping(value = "/input_fin", method = RequestMethod.GET)
		public String Youtubeinput(Data data, Model model, BindingResult result){
		if(result.hasErrors()) {
			return "/favodeli/Menu2";
		}
		datadao.insert(data);
		return MainMenu(model);
		}

	@RequestMapping(value = "/search", method = RequestMethod.GET)
	public String YoutubeEdit(Data data, Model model){

		model.addAttribute("searchItem",datadao.search(data));
		return "/favodeli/edit";
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST,params="updatebtn")
	public String dataupdate(Data data, Model model,BindingResult result){
		if(result.hasErrors()) {
			return "/favodeli/update";
		}
		datadao.update(data);
		return MainMenu(model);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST,params="deletebtn")
	public String datadelete(Data data, Model model){
		datadao.delete(data);
		return MainMenu(model);
	}

	@RequestMapping("/youtubeMenu")
	public String youtubeMenu() {
		return "/favodeli/youtubeMenu";
	}

	@RequestMapping("/test1")
	public String show99(Model model) {
	/*	int youso = DBE.count() - 1;
		DBE.select();

		vidList = new String[youso];
		vidList = DBE.vidList;
		titleList = new String[youso];
		titleList = DBE.titleList;
		samuneList = new String[youso];
		samuneList = DBE.samuneList;

		model.addAttribute("vidList",vidList);
		model.addAttribute("titleList",titleList);
		model.addAttribute("samuneList",samuneList);*/


		return "/favodeli/test1";
	}

	@RequestMapping("/hello")
	public String show999(Model model) {

		return "/favodeli/hello";
	}



}






