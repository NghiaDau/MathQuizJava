function getCachedData(key) {
    const cachedData = localStorage.getItem(key);
    return cachedData ? JSON.parse(cachedData) : null;
}

function setCachedData(key, data) {
    localStorage.setItem(key, JSON.stringify(data));
}

function clearMenuCache(key) {
    localStorage.removeItem(key);
}

function dataMenus() {
    return fetch("http://localhost:8080/api/menu")
        .then(function (response) {
            return response.json();
        })
        .then(function (menus) {
            setCachedData('cachedMenus', menus);
            return menus;
        })
        .catch(function (error) {
            console.error('Error fetching menus:', error);
        });
}

function refresh(){
    const menuContainer = document.getElementById("menu");
    menuContainer.innerHTML = '';
    clearMenuCache('cachedMenus');
    dataMenus().then(function (menus) {
        console.log('Fetched menus from API',menus);
        displayMenuIds(menus, menuContainer);
    });

}
// clearMenuCache('cachedMenus');
let cachedMenus = getCachedData('cachedMenus');
const menuContainer = document.getElementById("menu");
if (cachedMenus) {
    const menuContainer = document.getElementById("menu");
    menuContainer.innerHTML = '';
    console.log('Using cached menus', cachedMenus );
    displayMenuIds(cachedMenus, menuContainer);
} else {
    dataMenus().then(function (menus) {
        console.log('Fetched menus from API', menus);
        displayMenuIds(menus, menuContainer);
    });
}

function displayMenuIds(data, parentElement) {
    data.forEach(item => {
        if (item.level >= 3) {
            parentElement.style.top = '0%';
            parentElement.style.left = '160px';
            parentElement.style.position = 'absolute';
        }

        const li = document.createElement('li');
        li.className = 'mega';

        const a = document.createElement('a');
        a.className = 'waves-effect waves-light';
        a.href = item.url;
        if (item.level > 1) {
            li.style.width = '160px';
            li.style.height = '42px';
        }

        const span = document.createElement('span');
        span.className = 'hidden-xs';
        span.textContent = item.name;
        a.appendChild(span);
        li.appendChild(a);
        li.id = item.id_menu;
        parentElement.appendChild(li);

        if (item.children && item.children.length > 0) {
            const ul = document.createElement('ul');
            ul.classList.add('hidden');
            displayMenuIds(item.children, ul);
            li.appendChild(ul);

            li.addEventListener('mouseover', () => {
                ul.classList.remove('hidden');
            });

            ul.addEventListener('mouseout', () => {
                ul.classList.add('hidden');
            });
        }
    });
}

//Session
// function getSessionData(key) {
//     const sessionData = sessionStorage.getItem(key);
//     return sessionData ? JSON.parse(sessionData) : null;
// }
//
// function setSessionData(key, data) {
//     sessionStorage.setItem(key, JSON.stringify(data));
// }
//
// function clearSessionData(key) {
//     sessionStorage.removeItem(key);
// }
//
// function dataMenus() {
//     return fetch("http://localhost:8080/api/menu")
//         .then(function (response) {
//             return response.json();
//         })
//         .then(function (menus) {
//             setSessionData('cachedMenus', menus); // Lưu vào sessionStorage thay vì localStorage
//             return menus;
//         })
//         .catch(function (error) {
//             console.error('Error fetching menus:', error);
//         });
// }
//
// //clearSessionData('cachedMenus');
// let cachedMenus = getSessionData('cachedMenus'); // Sử dụng getSessionData thay vì getCachedData
// const menuContainer = document.getElementById("menu");
// if (cachedMenus) {
//     console.log('Using cached menus from sessionStorage:', cachedMenus);
//     displayMenuIds(cachedMenus, menuContainer);
// } else {
//     dataMenus().then(function (menus) {
//         console.log('Fetched menus from API and stored in sessionStorage:', menus);
//         displayMenuIds(menus, menuContainer);
//     });
// }

