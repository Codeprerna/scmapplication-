console.log("Sript loaded");


let currentTheme = getTheme();

// document.addEventListener("DOMContentLoaded",() => {
//     changeTheme();
// });

changeTheme();

//TODO
function changeTheme() {
   changePageTheme(currentTheme,currentTheme);

    // set the listener
    const changeThemeButton = document.querySelector("#theme_change_button");

    
    changeThemeButton.addEventListener("click",(event) =>{
        let oldTheme = currentTheme;
        if(currentTheme === "light") {
            currentTheme = "dark";
        } else {
            currentTheme = "light";
        }
        
        changePageTheme(currentTheme , oldTheme);
    });
}

//set theme to localstorage
function setTheme(theme) {
    localStorage.setItem("theme", theme);
}

//get theme from localstorage
function getTheme() {
   let theme =localStorage.getItem("theme");
   return theme? theme: "light";
   
}

//change current page theme
function changePageTheme(theme,oldTheme) {
    document.querySelector("html").classList.remove(oldTheme);
        document.querySelector("html").classList.add(theme);
        setTheme(theme);
        document
        .querySelector("#theme_change_button")
        .querySelector("span").textContent=
        theme == "light" ? " Dark" : " Light";
}
    