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
            localStorage.setItem('cachedMenus', JSON.stringify(menus));
            return menus;
        })
        .catch(function (error) {
            console.error('Error fetching menus:', error);
        });
}

//clearMenuCache('cachedMenus');
let cachedMenus = getCachedData('cachedMenus');
const menuContainer = document.getElementById("menu");
if (cachedMenus) {
    console.log('Using cached menus:', cachedMenus);
    displayMenuIds(cachedMenus, menuContainer);
} else {
    dataMenus().then(function (menus) {
        console.log('Fetched menus from API:', menus);
        displayMenuIds(menus, menuContainer);
    });
}

function displayMenuIds(data, parentElement) {
    data.forEach(item => {
        if (item.level >= 3) {
            parentElement.style.top = '0%';
            parentElement.style.left = '100px';
            parentElement.style.position = 'absolute';
        }
        const li = document.createElement('li');
        li.className = 'mega';

        const a = document.createElement('a');
        a.className = 'waves-effect waves-light';
        a.href = item.url;

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

            ul.addEventListener('mouseover', () => {
                ul.classList.add('hidden');
            });

        }
        parentElement.appendChild(li);
    });
}