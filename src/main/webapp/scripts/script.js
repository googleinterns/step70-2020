const API_KEY = '';
const YOUTUBE_DISCOVERY = 'https://www.googleapis.com/discovery/v1/apis/youtube/v3/rest';

/**
 * Loads any Google APIs listed in the discoveryDocs
 * @return {Promise} for completing the load
 */
function loadApi() {
  return new Promise((resolve, reject) => {
    gapi.load('client', () => {
      gapi.client.init({
        'apiKey': API_KEY,
        'discoveryDocs': [YOUTUBE_DISCOVERY]
      })
      .then(resolve)
      .catch((err) => {
        alert('Oops! An error occured with Youtube, please try again later.');
        console.error('Was not able to load Google API client:', err);
        reject();
      });
    });
  });
}

const TITLE_TEXT = 'Capstone Project' //TODO: change to actual name
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

function addOptionToSelectList(value, text, selectId) {
  const selectionDOM = document.getElementById(selectId);
  const option = document.createElement('option');
  option.value = value;
  option.innerText = text;
  selectionDOM.appendChild(option);
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
