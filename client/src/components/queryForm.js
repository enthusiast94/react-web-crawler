import React from 'react';

export default class QueryForm extends React.Component {

    state = {
        url: "",
        depth: 1
    }

    onUrlChange = (event) => {
        this.setState(Object.assign({}, this.state, {
            url: event.target.value
        }));
    }

    onDepthChange = (event) => {
        this.setState(Object.assign({}, this.state, {
            depth: event.target.value
        }));
    }

    onSubmit = (event) => {
        event.preventDefault();
        this.props.onSubmit(this.state.url, this.state.depth);
    }

    render() {
        return (
            <form onSubmit={this.onSubmit} style={{ display: "flex", flexDirection: "row" }}>
                <div className="form-group" style={{ flexGrow: 7 }}>
                    <label htmlFor="urlInput">Base URL</label>
                    <input id="urlInput" className="form-control form-control-sm" placeholder="Enter URL" onChange={this.onUrlChange} value={this.state.url} />
                </div>
                <div className="form-group" style={{ marginLeft: "10px", flexGrow: 1 }}>
                    <label htmlFor="depthSelect">Crawl depth</label>
                    <select id="depthSelect" className="form-control form-control-sm" value={this.state.depth} onChange={this.onDepthChange}>
                        <option>1</option>
                        <option>2</option>
                        <option>3</option>
                    </select>
                </div>
                <div className="form-group" style={{ marginLeft: "10px", flexGrow: 1 }}>
                    <label style={{ visibility: "hidden" }} htmlFor="submitButton">Hidden</label>
                    {this.props.isLoading ?
                        <button id="submitButton" className="form-control btn btn-sm btn-primary" type="submit" disabled>Submit</button> :
                        <button id="submitButton" className="form-control btn btn-sm btn-primary" type="submit">Submit</button>}
                </div>
            </form>
        )
    }
} 