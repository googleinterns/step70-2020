chrome.runtime.onInstalled.addListener(function() {
  chrome.declarativeContent.onPageChanged.removeRules(undefined, function() {
    chrome.declarativeContent.onPageChanged.addRules([{
      // required conditions for the extension to be active
       conditions: [new chrome.declarativeContent.PageStateMatcher({
        pageUrl: {hostEquals: 'www.youtube.com'},
       })],
      // things to do if the extension is active
      actions: [new chrome.declarativeContent.ShowPageAction()]
    }]);
  });
});
