const VIDEO_LINK = 'https://www.youtube.com/watch?v=testvideoid';
const SCORE = 0.05;
let fetchStub = null;

QUnit.module('Updating DOM after analyzing video', {
  beforeEach: function() {
    document.getElementById('sentiment-container').innerText = '';
    fetchStub = sinon.stub(window, 'fetch');
  },
  afterEach: function() {
    fetch.restore();
  }
});

QUnit.testStart(() => {
  document.getElementById('video-url').innerText = VIDEO_LINK;
});

QUnit.test('DOM updates with sentiment score', function (assert) {
  const body = { 'score': SCORE, 'scoreAvailable': true };
  const response = { json: () => { return body }, status: 200 };
  fetchStub.returns(Promise.resolve(response));

  const done = assert.async();
  analyzeVideo()
  .then((value) => {
    assert.dom('#sentiment-container')
        .hasProperty('innerText',SCORE.toString());
    done();
  });
});

QUnit.test('DOM updates with error', function (assert) {
  const response = { status: 500, statusText: '>299 error' };
  fetchStub.returns(Promise.resolve(response));

  const done = assert.async();
  analyzeVideo()
  .then((value) => {
    assert.dom('#sentiment-container').hasProperty('innerText','>299 error');
    done();
  });
});

QUnit.test('DOM updates without score', function (assert) {
  const body = { 'scoreAvailable': false };
  const response = { json: () => { return body }, status: 200 };
  fetchStub.returns(Promise.resolve(response));

  const done = assert.async()
  analyzeVideo()
  .then((value) => {
    assert.dom('#sentiment-container')
        .hasProperty('innerText','We couldn\'t analyze this video! Try again.');
    done();
  });
});