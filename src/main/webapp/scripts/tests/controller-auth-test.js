import * as view from '/scripts/view.js';
import * as controller from '/scripts/controller-auth.js';

// Test (controller-auth.js and) view.js
QUnit.module('Authorization controller', {
  beforeEach: () => {
    gapi = new CustomGapi();
  },
  afterEach: () => {
    sinon.restore();
  }
});

QUnit.test('When init fails, displays the authorization button and the error message', async function (assert) {
  const errorMsg = 'error message';
  sinon.stub(gapi.client, 'init').returns(Promise.reject(new Error(errorMsg)));

  await controller.initClient();

  assert.dom('#result-text').hasProperty('innerText', 'Failed to initialize client');
});

QUnit.test('When signed in, there should be sign-out and comment buttons', function (assert) {
  const fakeSignOut = sinon.fake();
  const fakeHandleCommentClick = sinon.fake();

  view.updateSigninStatus(true, () => { }, fakeSignOut, fakeHandleCommentClick);

  assert.dom('#authorize-button').hasProperty('innerText', 'Sign out');
  assert.dom('#authorize-button').hasProperty('onclick', fakeSignOut);
  assert.dom('#comment-button').hasProperty('onclick', fakeHandleCommentClick);
});

QUnit.test('When signed out, there should be an authorization button', function (assert) {
  const fakeAuth = sinon.fake();
  const fakeHandleCommentClick = sinon.fake();

  view.updateSigninStatus(false, fakeAuth, () => { }, fakeHandleCommentClick);

  assert.dom('#authorize-button').hasProperty('innerText', 'Authorize');
  assert.dom('#authorize-button').hasProperty('onclick', fakeAuth);
  assert.dom('#comment-button').doesNotExist();
});

QUnit.test('When postVideoComment fails, the error message should be displayed', async function (assert) {
  const errorMsg = 'error message';
  sinon.stub(gapi.client, 'request');
  gapi.client.request.returns(Promise.reject(new CustomError(errorMsg)));

  await controller.handleCommentClick();

  assert.dom('#result-text').hasProperty('innerText', `Error: ${errorMsg}`);
});

QUnit.test('When postVideoComment succeeds, the success message should be displayed', async function (assert) {
  sinon.stub(gapi.client, 'request');
  gapi.client.request.returns(Promise.resolve(new CustomResponse()));

  await controller.handleCommentClick();

  assert.dom('#result-text').hasProperty('innerText', `Successful`);
});



// Helper functions and classes
class CustomGapi {
  constructor() {
    this.client = new CustomClient();
  }
}

class CustomClient {
  constructor() { }
  request() { }
  init() { }
}

class CustomResponse extends Response {
  constructor() {
    super();
    this.json = {};
  }
}

class CustomError extends Error {
  constructor(msg) {
    super();
    this.result = { 'error': { 'message': msg, } };
  }
}