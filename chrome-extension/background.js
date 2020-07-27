const MS_BEFORE_CLEAR_NOTIFICAITON = 2000;
const VIBE_CHECK_MENU_ID = 'vibeCheck';
const TOXICITY_CHECK_MENU_ID = 'toxicityCheck';
const ICON_PATH = '/images/temp-icon.png';

chrome.runtime.onInstalled.addListener(function () {
  // "undefined" removes all rules
  chrome.declarativeContent.onPageChanged.removeRules(undefined, function () {
    chrome.declarativeContent.onPageChanged.addRules([{
      // required conditions for the extension to be active
      conditions: [new chrome.declarativeContent.PageStateMatcher({
        pageUrl: { hostEquals: 'www.youtube.com' },
      })],
      // things to do if the extension is active
      actions: [new chrome.declarativeContent.ShowPageAction()]
    }]);
  });
});

chrome.runtime.onInstalled.addListener(function () {
  const createProperties = {
    'id': VIBE_CHECK_MENU_ID,
    'title': 'Vibe check this video',
    'contexts': ['link'],
  };

  chrome.contextMenus.create(createProperties);
});

chrome.runtime.onInstalled.addListener(function () {
  const createProperties = {
    'id': TOXICITY_CHECK_MENU_ID,
    'title': 'Toxicity check this line',
    'contexts': ['selection'],
  };

  chrome.contextMenus.create(createProperties);
});

chrome.contextMenus.onClicked.addListener(async function (info) {
  if (info.menuItemId == VIBE_CHECK_MENU_ID) {
    await handleVibeCheck(info);
  } else if (info.menuItemId == TOXICITY_CHECK_MENU_ID) {
    await handleToxicityCheck(info);
  }
});

async function handleVibeCheck(info) {
  const notificationOption = {
    type: 'basic',
    iconUrl: ICON_PATH,
    title: await getSentimentAnaylsisMsg(info.linkUrl),
    message: `Click to watch ${info.linkUrl}`,
    priority: 0
  };

  chrome.notifications.create(notificationOption, function (id) {
    chrome.notifications.onClicked.addListener(function () {
      chrome.tabs.create({ url: info.linkUrl });
    });
    setTimeout(function () { chrome.notifications.clear(id); }, MS_BEFORE_CLEAR_NOTIFICAITON);
  });
}

async function handleToxicityCheck(info) {
  const notificationOption = {
    type: 'basic',
    iconUrl: ICON_PATH,
    title: await getToxicityAnaylsisMsg(info.selectionText),
    message: `"${info.selectionText}"`,
    priority: 0
  };

  chrome.notifications.create(notificationOption, function (id) {
    setTimeout(function () { chrome.notifications.clear(id); }, MS_BEFORE_CLEAR_NOTIFICAITON);
  });
}

/**  @param {String} linkUrl, unchecked */
async function getSentimentAnaylsisMsg(linkUrl) {
  // TODO calls backend
  return 'Sentiment score is 42';
}

/**  @param {String} text, unchecked */
async function getToxicityAnaylsisMsg(text) {
  // TODO calls backend
  return 'Toxicity score is 24';
}