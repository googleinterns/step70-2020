import { postVideoComment } from '/scripts/post-comment.js';
import { videoId, commentElement, failedInit, successfulApiCallback, failedApiCallback, updateSigninStatus } from '/scripts/view.js';

const apiKey = '';
const clientId = '';
const discoveryDocs = ['https://www.googleapis.com/discovery/v1/apis/youtube/v3/rest'];
const scope = 'https://www.googleapis.com/auth/youtube.force-ssl';

function handleClientLoad() {
  gapi.load('client:auth2', initClient);
}

function initClient() {
  gapi.client.init({
    apiKey,
    discoveryDocs,
    clientId,
    scope
  })
    .then(function () {
      // Listen for sign-in state changes.
      gapi.auth2.getAuthInstance().isSignedIn.listen((isSignedIn) =>
        updateSigninStatus(isSignedIn, handleAuthClick, handleSignoutClick, handleCommentClick));

      // Handle the initial sign-in state.
      updateSigninStatus(gapi.auth2.getAuthInstance().isSignedIn.get(),
        handleAuthClick, handleSignoutClick, handleCommentClick);
    })
    .catch(failedInit);
}

function handleAuthClick(event) {
  gapi.auth2.getAuthInstance().signIn();
}

function handleSignoutClick(event) {
  gapi.auth2.getAuthInstance().signOut();
}

function handleCommentClick(event) {
  postVideoComment(videoId, postCommentElement.value)
    .then(successfulApiCallback)
    .catch(failedApiCallback);
}

// Entry point
handleClientLoad();