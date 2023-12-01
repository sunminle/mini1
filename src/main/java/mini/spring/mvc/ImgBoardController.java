package mini.spring.mvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import mini.spring.mvc.bean.ImgBoardDTO;
import mini.spring.mvc.service.ImgBoardService;

@Controller
@RequestMapping("/imgboard/*")
public class ImgBoardController {
	@Autowired
	private ImgBoardService service;
	
	
	@RequestMapping("list")
	public String test(Model model, @RequestParam(value="pageNum", defaultValue = "1") int pageNum) {
		service.list(pageNum,model);

		return "imgboard/list";
	}
	
	@RequestMapping("writeForm")
	public String writeForm() {

		return "imgboard/writeForm";
	}
	
	@RequestMapping("writePro")
	public String writePro(Model model,ImgBoardDTO dto,ArrayList<MultipartFile> files,HttpServletRequest requset) {
		int isfile = 0;
		int result = 0;
		int num = 0;
		String firstImg = "firstimgDefault.jpg";
		for(MultipartFile f : files) {
			if(!f.getOriginalFilename().equals("")) {
				isfile ++;
			}
		}
		dto.setIsfile(isfile);
		if(isfile > 0) {
			String filePath = requset.getServletContext().getRealPath("/resources/file/imgBoard/");
			result = service.fileInsert(files,filePath);
			num = service.maxNum()+1;
			firstImg = service.firstImgSelect(num);
			
		}
		dto.setFirstimg(firstImg);
		service.insert(dto);
		return "imgboard/writePro";
	}
	
	@RequestMapping("content")
	public String content(Model model, int num, int pageNum) {
		List<String> fileNameList = Collections.EMPTY_LIST;
		service.readcountUp(num);
		ImgBoardDTO dto = service.content(num);
		if(dto.getIsfile()!=0) {
			fileNameList = service.fileName(num);
		}
		model.addAttribute("dto",dto);
		model.addAttribute("pageNum",pageNum);
		model.addAttribute("fileNameList",fileNameList);
		model.addAttribute("num",num);
		return "imgboard/content";
	}
	
	@RequestMapping("reviewPro")
	public String reviewPro(int num, Model model, String content) {
		model.addAttribute("num",num);
		model.addAttribute("content",content);
		return "imgboard/reviewPro";
	}
	
}
