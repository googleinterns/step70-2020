const TITLE_TEXT = 'Capstone Project' //TODO: change to actual name
const MENU_ITEMS = [
  new MenuItem('Trending', 'popular.html', 'whatshot'),
  new MenuItem('Positive', 'positive.html', 'grade'),
  new MenuItem('Check', 'index.html', 'check_circle')
];

createHeader();
createMenu();

function MenuItem (name, link, icon) {
  this.name = name;
  this.link = link;
  this.icon = icon;
}

function createHeader() {
  const headerDom = document.getElementsByTagName('header')[0];
  const title = document.createElement('h3');
  const link = document.createElement('a');
  title.innerText = TITLE_TEXT;
  link.className = 'navbar-brand';
  link.href = 'index.html';
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
