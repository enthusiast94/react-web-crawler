import React from 'react';
import ReactDOM from 'react-dom';
import * as d3 from "d3";
import './siteMap.css';

export default class SiteMap extends React.Component {

    componentDidMount() {
        const nodes = this.computeDistinctNodesFromLinks(this.props.links);

        const width = 1000;
        const height = 600;

        const force = d3.layout.force()
            .nodes(d3.values(nodes))
            .links(this.props.links)
            .size([width, height])
            .linkDistance(240)
            .charge(-200)
            .on("tick", tick)
            .start();

        const svg = d3.select(ReactDOM.findDOMNode(this)).append("svg")
            .attr("width", width)
            .attr("height", height);

        // Per-type markers, as they don't inherit styles.
        svg.append("defs").selectAll("marker")
            .data(["arrowHead"])
            .enter().append("marker")
            .attr("id", function (d) { return d; })
            .attr("viewBox", "0 -5 10 10")
            .attr("refX", 15)
            .attr("refY", -1.5)
            .attr("markerWidth", 6)
            .attr("markerHeight", 6)
            .attr("orient", "auto")
            .append("path")
            .attr("d", "M0,-5L10,0L0,5");

        const path = svg.append("g").selectAll("path")
            .data(force.links())
            .enter().append("path")
            .attr("class", function (d) { return "link"; })
            .attr("marker-end", function (d) { return "url(#arrowHead)"; });

        const circle = svg.append("g").selectAll("circle")
            .data(force.nodes())
            .enter().append("circle")
            .attr("r", 6)
            .call(force.drag);

        const text = svg.append("g").selectAll("text")
            .data(force.nodes())
            .enter().append("text")
            .attr("x", 8)
            .attr("y", ".31em")
            .text(function (d) { return d.name; });

        // Use elliptical arc path segments to doubly-encode directionality.
        function tick() {
            path.attr("d", linkArc);
            circle.attr("transform", transform);
            text.attr("transform", transform);
        }

        function linkArc(d) {
            const dx = d.target.x - d.source.x;
            const dy = d.target.y - d.source.y;
            const dr = Math.sqrt(dx * dx + dy * dy);
            return "M" + d.source.x + "," + d.source.y + "A" + dr + "," + dr + " 0 0,1 " + d.target.x + "," + d.target.y;
        }

        function transform(d) {
            return "translate(" + d.x + "," + d.y + ")";
        }
    }

    render() {
        return (
            <div className="sitemap-container"></div>
        );
    }

    computeDistinctNodesFromLinks(links) {
        const nodes = {};
        links.forEach(function (link) {
            link.source = nodes[link.source] || (nodes[link.source] = { name: link.source });
            link.target = nodes[link.target] || (nodes[link.target] = { name: link.target });
        });

        return nodes;
    }
} 