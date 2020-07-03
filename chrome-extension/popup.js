
const button = document.getElementById('website');

button.onclick = function() {
  chrome.tabs.create({url: "https://step70-2020.appspot.com/"});
};
