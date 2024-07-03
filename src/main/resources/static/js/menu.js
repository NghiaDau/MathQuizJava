fetch("http://localhost:8080/api/menu")
    .then(function (data) {
        return data.json();
    }).then(function (menus) {
    const menuContainer = document.getElementById("menu");
    displayMenuIds(menus, menuContainer);
    console.log(menus);
});

// function displayMenuIds(data, parentElement) {
//     data.forEach(item => {
//         const li = document.createElement('li');
//         li.textContent = item.name;
//         li.id = item.id_menu;
//         if (item.children && item.children.length > 0) {
//             const ul = document.createElement('ul');
//             ul.classList.add('hidden');
//             displayMenuIds(item.children, ul);
//             li.appendChild(ul);
//
//             li.addEventListener('mouseover', () => {
//                 ul.classList.remove('hidden');
//             });
//
//             li.addEventListener('mouseout', () => {
//                 ul.classList.add('hidden');
//             });
//         }
//
//         parentElement.appendChild(li);
//     });
// }
function displayMenuIds(data, parentElement) {
    data.forEach(item => {
        const li = document.createElement('li');
        li.className = 'mega';

        const a = document.createElement('a');
        a.className = 'waves-effect waves-light';
        a.href = '/login';  // Adjust this as needed, you might want to use item.url if available

        const span = document.createElement('span');
        span.className = 'hidden-xs';
        span.textContent = item.name;


        a.appendChild(span);
        li.appendChild(a);
        li.id = item.id_menu;

        if (item.children && item.children.length > 0) {
            const ul = document.createElement('ul');
            ul.classList.add('hidden');
            displayMenuIds(item.children, ul);
            li.appendChild(ul);

            li.addEventListener('mouseover', () => {
                ul.classList.remove('hidden');
            });

            li.addEventListener('mouseout', () => {
                clearTimeout(timeoutId);
            });
        }

        parentElement.appendChild(li);
    });
}