const API_KEY = '';

const TITLE_TEXT = 'Youtube Vibe Check';
const MENU_ITEMS = [
  {name: 'Trending', link: 'popular.html', icon: 'whatshot'},
  {name: 'Positive', link: 'positive.html', icon: 'grade'},
  {name: 'Check', link: 'index.html', icon: 'check_circle'}
];

function updateDom(text, containerId) {
  const containerDOM = document.getElementById(containerId);
  containerDOM.innerText = text;
}

function getDomValue(domId) {
  return document.getElementById(domId).value;
}

function createHeader() {
  const headerDom = document.getElementsByTagName('header')[0];
  const title = document.createElement('h3');
  const logo = document.createElement('img');
  const link = document.createElement('a');
  title.innerText = TITLE_TEXT;
  title.className = 'd-inline-block align-top';
  logo.src = 'logo.png';
  logo.id = 'logo';
  link.className = 'navbar-brand';
  link.href = 'index.html';
  link.appendChild(logo);
  link.appendChild(title);
  headerDom.appendChild(link);
}

function createMenu() {
  const menuDom = document.getElementById('menu');
  for(item of MENU_ITEMS) {
    const listItem = document.createElement('li');
    const icon = document.createElement('i');
    const link = document.createElement('a');
    const label = document.createTextNode(item.name);
    listItem.className = 'nav-item py-3';
    icon.className = 'material-icons md-dark';
    link.className = 'nav-link';

    icon.innerText = item.icon;
    link.href = item.link;

    link.appendChild(icon);
    link.appendChild(label);
    listItem.appendChild(link);
    menuDom.appendChild(listItem);
  }
}
