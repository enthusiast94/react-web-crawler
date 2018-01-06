import React from 'react';
import QueryForm from '../components/queryForm';
import PropTypes from 'prop-types';
import { fetchSitemap } from '../services/sitemapService';
import SiteMap from '../components/siteMap';

export default class App extends React.Component {

    static contextTypes = {
        store: PropTypes.object
    };

    store = this.context.store;

    componentDidMount() {
        this.store.subscribe(() => this.forceUpdate());
    }

    render() {
        const state = this.store.getState();
        console.log(state);

        return (
            <div>
                <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
                    <div className="container">
                        <a className="navbar-brand" href="#">Multithreaded Web Crawler</a>
                    </div>
                </nav>
                <br/>
                <div className="container">
                    <QueryForm onSubmit={this.onQuerySubmit} isLoading={state.isFetching} />
                    {state.isFetching && <p>Loading...</p>}
                    {state.error && <p className="text-danger"><strong>Error:</strong> {state.error}</p>}
                    {state.sitemap && <SiteMap sitemap={state.sitemap} />}
                </div>
            </div>
        );
    }

    onQuerySubmit = (url, depth) => {
        fetchSitemap(this.store, url, depth);
    }
}