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
  console.log('sentiment score');
  const body = { 'score': SCORE, 'scoreAvailable': true };
  const response = { json: () => { return body }, status: 200, ok: true };
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
  console.log('error');
  const response = { status: 500, statusText: '>299 error', ok: false };
  fetchStub.returns(Promise.resolve(response));

  const done = assert.async();
  analyzeVideo()
  .then((value) => {
    assert.dom('#sentiment-container').hasProperty('innerText','>299 error');
    done();
  });
});

QUnit.test('DOM updates without score', function (assert) {
  console.log('no score');
  const body = { 'scoreAvailable': false };
  const response = { json: () => { return body }, status: 200, ok: true };
  fetchStub.returns(Promise.resolve(response));

  const done = assert.async()
  analyzeVideo()
  .then((value) => {
    assert.dom('#sentiment-container').hasProperty('innerText',
        'Video has no available captions or comments to analyze.');
    done();
  });
});