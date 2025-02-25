package dawidbialek.spring_crud_app.controller;

import dawidbialek.spring_crud_app.service.BookService;
import dawidbialek.spring_crud_app.model.BookModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    @Autowired
    BookService bookService;

    @GetMapping
    public String getBooksPage(@RequestParam(required = false) String login,
                               @RequestParam(required = false) String email,
                               Model model,
                               HttpServletRequest request){

        HttpSession session = request.getSession();
        if(login != null && !login.isEmpty()){
            session.setAttribute("userLogin", login);
        }

        String userLogin = (String) session.getAttribute("userLogin");

        model.addAttribute("userLogin", userLogin);

        List<BookModel> books = bookService.getAllBooksByLogin(login);

        model.addAttribute("userBooks", books);

        return "book_page";
    }

    @GetMapping("/create")
    public String getCreateBookPage(Model model){
        model.addAttribute("newBook", new BookModel());
        return "create_book_page";
    }

    @PostMapping("/createBook")
    public String createBook(@ModelAttribute BookModel book){
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/edit/{title}")
    public String getEditBookPage(Model model, @PathVariable String title){
        BookModel byTitle = bookService.findByTitleAndDelete(title);
        model.addAttribute("bookToEdit", byTitle);
        return "edit_book_page";
    }

    @PostMapping("/editBook")
    public String editBook(@ModelAttribute BookModel book){
        bookService.edit(book);
         return "redirect:/books";
    }

    @GetMapping("/delete/{title}")
    public String delete(@PathVariable String title){
        bookService.delete(title);
        return "redirect:/books";
    }
}
