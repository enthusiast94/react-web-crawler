import { fetchSitemapRequest, fetchSitemapSuccess, fetchSitemapFailure } from '../redux/actions';

export function fetchSitemap(store, url, depth) {
    store.dispatch(fetchSitemapRequest());
    return fetch(`/api/crawl?url=${url}&depth=${depth}`)
        .then(response => {
            response.json().then(json => {
                if (!response.ok) {
                    return store.dispatch(fetchSitemapFailure(json.error));
                }

                return store.dispatch(fetchSitemapSuccess(json.data));
            });
        }).catch(error => store.dispatch(fetchSitemapFailure(error)));
}