import popular from '../popular.js';

const FAKE_ERROR = new Error('error message');

const mockGetTrendingResponse = {
  items: ['Example Title']
};

QUnit.module("list videos", {
  beforeEach: function() {
    // reset container before running test
    document.getElementById('popular-list-container').innerText = '';
    loadApi = sinon.fake.returns(
      Promise.resolve()
    );
  },
  afterEach: function() {
    sinon.restore();
  },
});

QUnit.test('loadPopular - normal use', function(assert) {
  const done = assert.async();
  const getTrendingFromYoutubeApi = sinon.fake.returns(
    Promise.resolve(mockGetTrendingResponse)
  );
  sinon.replace(popular, 'getTrendingFromYoutubeApi', getTrendingFromYoutubeApi);
  popular.loadPopular().then(() => {
    assert.dom('#popular-list-container').hasText(mockGetTrendingResponse.items[0]);
    done();
  });
});

QUnit.test('loadPopular - API error', function(assert) {
  const done = assert.async();
  const getTrendingFromYoutubeApi = sinon.fake.returns(
    Promise.reject(FAKE_ERROR)
  );
  sinon.replace(popular, 'getTrendingFromYoutubeApi', getTrendingFromYoutubeApi);
  popular.loadPopular().then(() => {
    assert.dom('#popular-list-container').hasText('An error occured with YouTube');
    done();
  });
});
