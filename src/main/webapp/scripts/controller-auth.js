import { postVideoComment } from '/scripts/post-comment.js';
import { getVideoId, postCommentElement, failedInitCallback, successfulApiCallback, failedApiCallback, updateSigninStatus } from '/scripts/view.js';

/** API key provied by GCP to authenticate the client */
const apiKey = '';
/** client ID provied by GCP to identify the client */
const clientId = '';
/** discovery docs used to identify API endpoints */
const discoveryDocs = ['https://www.googleapis.com/discovery/v1/apis/youtube/v3/rest'];
/** OAuth2 scope required to post a comment */
const scope = 'https://www.googleapis.com/auth/youtube.force-ssl';

/** Load the client from Google API oauth2.0 JS library */
function handleClientLoad() {
  gapi.load('client:auth2', initClient);
}

/**
 * Initialize the client with appropiate fields.
 * And listen for changed in the signin status.
 */
function initClient() {
  return gapi.client.init({
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
    .catch(failedInitCallback);
}

/**
 * Handler that initiates the authorization flow with a popup.
 * The user only needs to authorize the site once.
 */
function handleAuthClick(event) {
  gapi.auth2.getAuthInstance().signIn();
}

/** Handler that quietly signs the user out, but does not revoke the authorization */
function handleSignoutClick(event) {
  gapi.auth2.getAuthInstance().signOut();
}

/** Handler that posts the comment and processes the result */
function handleCommentClick(event) {
  return postVideoComment(getVideoId(), postCommentElement.value)
    .then(successfulApiCallback)
    .catch(failedApiCallback);
}

// Entry point
handleClientLoad();

// For testing purpose
export { initClient };
