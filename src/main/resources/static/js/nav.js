document.addEventListener("DOMContentLoaded", function() {
    fetch("http://localhost:8080/api/levels")
        .then(function(response) {
            return response.json();
        })
        .then(function(levels) {
            let menuContainer = document.getElementById("menu-container");
            let ul = document.createElement("ul");
            ul.className = "menu";
            levels.forEach(level => {
                let levelLi = createMenuItem(level.name);
                if (level.grades.length > 0) {
                    let gradesUl = document.createElement("ul");
                    level.grades.forEach(grade => {
                        let gradeLi = createMenuItem(grade.name);
                        if (grade.chapters.length > 0) {
                            let chaptersUl = document.createElement("ul");
                            grade.chapters.forEach(chapter => {
                                let chapterLi = createMenuItem(chapter.name);
                                chaptersUl.appendChild(chapterLi);
                            });
                            gradeLi.appendChild(chaptersUl);
                        }
                        gradesUl.appendChild(gradeLi);
                    });
                    levelLi.appendChild(gradesUl);
                }
                ul.appendChild(levelLi);
            });
            menuContainer.appendChild(ul);
        });

    function createMenuItem(text) {
        let li = document.createElement("li");
        li.textContent = text;
        return li;
    }
});