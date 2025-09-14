/******/ (() => { // webpackBootstrap
/******/ 	"use strict";
/******/ 	var __webpack_modules__ = ({

/***/ "./src/App.tsx":
/*!*********************!*\
  !*** ./src/App.tsx ***!
  \*********************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   "default": () => (__WEBPACK_DEFAULT_EXPORT__)
/* harmony export */ });
/* harmony import */ var _jsx_pragma__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./jsx/pragma */ "./src/jsx/pragma.ts");
/* harmony import */ var _jsx_observer__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./jsx/observer */ "./src/jsx/observer.ts");
/* harmony import */ var _components_Toast__WEBPACK_IMPORTED_MODULE_2__ = __webpack_require__(/*! ./components/Toast */ "./src/components/Toast.tsx");



var errors = new _jsx_observer__WEBPACK_IMPORTED_MODULE_1__.ArrayObserver(["err1", "err2", "err3"]);
var App = function () {
    return ((0,_jsx_pragma__WEBPACK_IMPORTED_MODULE_0__["default"])("div", null,
        (0,_jsx_pragma__WEBPACK_IMPORTED_MODULE_0__["default"])(_components_Toast__WEBPACK_IMPORTED_MODULE_2__["default"], { title: "Error", message: "abc" }),
        (0,_jsx_pragma__WEBPACK_IMPORTED_MODULE_0__["default"])(_components_Toast__WEBPACK_IMPORTED_MODULE_2__["default"], { title: "Error", message: "abc" }),
        (0,_jsx_pragma__WEBPACK_IMPORTED_MODULE_0__["default"])(_components_Toast__WEBPACK_IMPORTED_MODULE_2__["default"], { title: "Error", message: "abc" })));
};
/* harmony default export */ const __WEBPACK_DEFAULT_EXPORT__ = (App);


/***/ }),

/***/ "./src/components/Toast.scss":
/*!***********************************!*\
  !*** ./src/components/Toast.scss ***!
  \***********************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
// extracted by mini-css-extract-plugin


/***/ }),

/***/ "./src/components/Toast.tsx":
/*!**********************************!*\
  !*** ./src/components/Toast.tsx ***!
  \**********************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   "default": () => (/* binding */ Toast)
/* harmony export */ });
/* harmony import */ var _jsx_pragma__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ../jsx/pragma */ "./src/jsx/pragma.ts");
/* harmony import */ var _Toast_scss__WEBPACK_IMPORTED_MODULE_1__ = __webpack_require__(/*! ./Toast.scss */ "./src/components/Toast.scss");


function Toast(_a) {
    var title = _a.title, message = _a.message;
    return (0,_jsx_pragma__WEBPACK_IMPORTED_MODULE_0__["default"])("div", { className: "toast" },
        (0,_jsx_pragma__WEBPACK_IMPORTED_MODULE_0__["default"])("h1", null, title),
        (0,_jsx_pragma__WEBPACK_IMPORTED_MODULE_0__["default"])("p", null, message));
}
;


/***/ }),

/***/ "./src/jsx/observer.ts":
/*!*****************************!*\
  !*** ./src/jsx/observer.ts ***!
  \*****************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   ArrayObserver: () => (/* binding */ ArrayObserver),
/* harmony export */   Observer: () => (/* binding */ Observer)
/* harmony export */ });
var __extends = (undefined && undefined.__extends) || (function () {
    var extendStatics = function (d, b) {
        extendStatics = Object.setPrototypeOf ||
            ({ __proto__: [] } instanceof Array && function (d, b) { d.__proto__ = b; }) ||
            function (d, b) { for (var p in b) if (Object.prototype.hasOwnProperty.call(b, p)) d[p] = b[p]; };
        return extendStatics(d, b);
    };
    return function (d, b) {
        if (typeof b !== "function" && b !== null)
            throw new TypeError("Class extends value " + String(b) + " is not a constructor or null");
        extendStatics(d, b);
        function __() { this.constructor = d; }
        d.prototype = b === null ? Object.create(b) : (__.prototype = b.prototype, new __());
    };
})();
var __spreadArray = (undefined && undefined.__spreadArray) || function (to, from, pack) {
    if (pack || arguments.length === 2) for (var i = 0, l = from.length, ar; i < l; i++) {
        if (ar || !(i in from)) {
            if (!ar) ar = Array.prototype.slice.call(from, 0, i);
            ar[i] = from[i];
        }
    }
    return to.concat(ar || Array.prototype.slice.call(from));
};
var Observer = /** @class */ (function () {
    function Observer(initial) {
        this._listeners = new Set();
        this._value = initial;
    }
    Object.defineProperty(Observer.prototype, "value", {
        get: function () {
            return this._value;
        },
        set: function (newValue) {
            if (this._value !== newValue) {
                this._value = newValue;
                this.notify(newValue);
            }
        },
        enumerable: false,
        configurable: true
    });
    Observer.prototype.onChange = function (listener) {
        this._listeners.add(listener);
        listener(this._value);
    };
    Observer.prototype.notify = function (newValue) {
        this._listeners.forEach(function (listener) { return listener(newValue); });
    };
    return Observer;
}());
var ArrayObserver = /** @class */ (function (_super) {
    __extends(ArrayObserver, _super);
    function ArrayObserver(initial) {
        var _this = _super.call(this, initial) || this;
        _this.proxy = _this.createProxy(initial);
        return _this;
    }
    Object.defineProperty(ArrayObserver.prototype, "value", {
        get: function () {
            return this.proxy;
        },
        enumerable: false,
        configurable: true
    });
    ArrayObserver.prototype.createProxy = function (array) {
        var _this = this;
        var methodsToIntercept = [
            'push', 'pop', 'shift', 'unshift', 'splice',
            'sort', 'reverse', 'copyWithin', 'fill'
        ];
        return new Proxy(array, {
            get: function (target, prop) {
                if (methodsToIntercept.includes(prop)) {
                    return function () {
                        var _a;
                        var args = [];
                        for (var _i = 0; _i < arguments.length; _i++) {
                            args[_i] = arguments[_i];
                        }
                        var result = (_a = target)[prop].apply(_a, args);
                        _this.notify(__spreadArray([], target, true));
                        return result;
                    };
                }
                return target[prop];
            },
            set: function (target, prop, value) {
                var numericProp = Number(prop);
                if (!isNaN(numericProp) || prop === 'length') {
                    var oldValue = target[prop];
                    target[prop] = value;
                    if (oldValue !== value) {
                        _this.notify(__spreadArray([], target, true));
                    }
                }
                else {
                    target[prop] = value;
                }
                return true;
            }
        });
    };
    return ArrayObserver;
}(Observer));



/***/ }),

/***/ "./src/jsx/pragma.ts":
/*!***************************!*\
  !*** ./src/jsx/pragma.ts ***!
  \***************************/
/***/ ((__unused_webpack_module, __webpack_exports__, __webpack_require__) => {

__webpack_require__.r(__webpack_exports__);
/* harmony export */ __webpack_require__.d(__webpack_exports__, {
/* harmony export */   "default": () => (__WEBPACK_DEFAULT_EXPORT__)
/* harmony export */ });
/* harmony import */ var _observer__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./observer */ "./src/jsx/observer.ts");

function parseChildren(children) {
    return children.map(function (child) {
        if (typeof child === 'string') {
            return document.createTextNode(child);
        }
        return child;
    });
}
function parseNode(tag, properties, children) {
    var el = document.createElement(tag);
    if (properties.model !== undefined) {
        if (typeof properties.model === 'string') {
            console.log(properties[properties.model]);
            el.innerText = properties[properties.model];
        }
        else {
            var model = properties.model;
            model.onChange(function (value) {
                if (el instanceof HTMLInputElement) {
                    el.value = value;
                }
                else
                    el.innerText = value;
            });
        }
    }
    for (var key in properties) {
        var value = properties[key];
        // if (typeof value === "function") {
        //     el[key] = (event: Event) => {
        //         // Определяем сигнатуру функции и вызываем accordingly
        //         const functionLength = value.length;
        //
        //         if (value.length === 1) {
        //             return value({
        //                 event: event,
        //                 item: properties['data-item'],
        //             });
        //         }
        //
        //         return value()
        //     };
        // } else
        el[key] = properties[key];
    }
    parseChildren(children).forEach(function (child) {
        if (typeof child === 'function') {
            var child2 = child(properties);
            if (typeof child2 === 'string') {
                el.appendChild(document.createTextNode(child2));
            }
            else
                el.appendChild(child2);
        }
        else {
            el.appendChild(child);
        }
    });
    return el;
}
function jsx(element, properties) {
    var children = [];
    for (var _i = 2; _i < arguments.length; _i++) {
        children[_i - 2] = arguments[_i];
    }
    properties = properties || {};
    if (typeof element === 'function') {
        return element(properties, children);
    }
    if (properties.for !== undefined) {
        var iter_1 = properties.for;
        var key_1 = properties.forKey || "item";
        var fragment_1 = document.createDocumentFragment();
        if (iter_1 instanceof _observer__WEBPACK_IMPORTED_MODULE_0__.ArrayObserver) {
            var nodes_1 = [];
            var rerender_1 = function (items) {
                var parent;
                nodes_1.forEach(function (child) {
                    parent = child.parentNode;
                    parent.removeChild(child);
                });
                nodes_1 = [];
                items.forEach(function (item) {
                    properties[key_1] = item;
                    nodes_1.push(parseNode(element, properties, children.flat()));
                });
                nodes_1.forEach(function (node) {
                    if (parent)
                        parent.appendChild(node);
                    else
                        fragment_1.appendChild(node);
                });
            };
            iter_1.onChange(function (value) {
                rerender_1(iter_1.value);
            });
        }
        else {
            Array.from(iter_1)
                .forEach(function (el) {
                properties[key_1] = el;
                fragment_1.appendChild(parseNode(element, properties, children.flat()));
            });
        }
        return fragment_1;
    }
    return parseNode(element, properties, children.flat());
}
/* harmony default export */ const __WEBPACK_DEFAULT_EXPORT__ = (jsx);


/***/ })

/******/ 	});
/************************************************************************/
/******/ 	// The module cache
/******/ 	var __webpack_module_cache__ = {};
/******/ 	
/******/ 	// The require function
/******/ 	function __webpack_require__(moduleId) {
/******/ 		// Check if module is in cache
/******/ 		var cachedModule = __webpack_module_cache__[moduleId];
/******/ 		if (cachedModule !== undefined) {
/******/ 			return cachedModule.exports;
/******/ 		}
/******/ 		// Create a new module (and put it into the cache)
/******/ 		var module = __webpack_module_cache__[moduleId] = {
/******/ 			// no module.id needed
/******/ 			// no module.loaded needed
/******/ 			exports: {}
/******/ 		};
/******/ 	
/******/ 		// Execute the module function
/******/ 		__webpack_modules__[moduleId](module, module.exports, __webpack_require__);
/******/ 	
/******/ 		// Return the exports of the module
/******/ 		return module.exports;
/******/ 	}
/******/ 	
/************************************************************************/
/******/ 	/* webpack/runtime/define property getters */
/******/ 	(() => {
/******/ 		// define getter functions for harmony exports
/******/ 		__webpack_require__.d = (exports, definition) => {
/******/ 			for(var key in definition) {
/******/ 				if(__webpack_require__.o(definition, key) && !__webpack_require__.o(exports, key)) {
/******/ 					Object.defineProperty(exports, key, { enumerable: true, get: definition[key] });
/******/ 				}
/******/ 			}
/******/ 		};
/******/ 	})();
/******/ 	
/******/ 	/* webpack/runtime/hasOwnProperty shorthand */
/******/ 	(() => {
/******/ 		__webpack_require__.o = (obj, prop) => (Object.prototype.hasOwnProperty.call(obj, prop))
/******/ 	})();
/******/ 	
/******/ 	/* webpack/runtime/make namespace object */
/******/ 	(() => {
/******/ 		// define __esModule on exports
/******/ 		__webpack_require__.r = (exports) => {
/******/ 			if(typeof Symbol !== 'undefined' && Symbol.toStringTag) {
/******/ 				Object.defineProperty(exports, Symbol.toStringTag, { value: 'Module' });
/******/ 			}
/******/ 			Object.defineProperty(exports, '__esModule', { value: true });
/******/ 		};
/******/ 	})();
/******/ 	
/************************************************************************/
var __webpack_exports__ = {};
// This entry needs to be wrapped in an IIFE because it needs to be isolated against other modules in the chunk.
(() => {
/*!**********************!*\
  !*** ./src/index.ts ***!
  \**********************/
__webpack_require__.r(__webpack_exports__);
/* harmony import */ var _App__WEBPACK_IMPORTED_MODULE_0__ = __webpack_require__(/*! ./App */ "./src/App.tsx");

var root = document.getElementById("root");
root.appendChild((0,_App__WEBPACK_IMPORTED_MODULE_0__["default"])());

})();

/******/ })()
;
//# sourceMappingURL=data:application/json;charset=utf-8;base64,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QUM7WUFDekUsQ0FBQyxDQUFDO1FBQ1YsQ0FBQztRQUVELE9BQU8sVUFBUTtJQUNuQixDQUFDO0lBRUQsT0FBTyxTQUFTLENBQUMsT0FBTyxFQUFFLFVBQVUsRUFBRSxRQUFRLENBQUMsSUFBSSxFQUFFLENBQUMsQ0FBQztBQUMzRCxDQUFDO0FBRUQsaUVBQWUsR0FBRyxFQUFDOzs7Ozs7O1VDbEluQjtVQUNBOztVQUVBO1VBQ0E7VUFDQTtVQUNBO1VBQ0E7VUFDQTtVQUNBO1VBQ0E7VUFDQTtVQUNBO1VBQ0E7VUFDQTtVQUNBOztVQUVBO1VBQ0E7O1VBRUE7VUFDQTtVQUNBOzs7OztXQ3RCQTtXQUNBO1dBQ0E7V0FDQTtXQUNBLHlDQUF5Qyx3Q0FBd0M7V0FDakY7V0FDQTtXQUNBLEU7Ozs7O1dDUEEsd0Y7Ozs7O1dDQUE7V0FDQTtXQUNBO1dBQ0EsdURBQXVELGlCQUFpQjtXQUN4RTtXQUNBLGdEQUFnRCxhQUFhO1dBQzdELEU7Ozs7Ozs7Ozs7OztBQ053QjtBQUd4QixJQUFNLElBQUksR0FBRyxRQUFRLENBQUMsY0FBYyxDQUFDLE1BQU0sQ0FBQyxDQUFDO0FBQzdDLElBQUksQ0FBQyxXQUFXLENBQUMsZ0RBQUcsRUFBRSxDQUFDIiwic291cmNlcyI6WyJ3ZWJwYWNrOi8vbGFiMS1mcm9udGVuZC8uL3NyYy9BcHAudHN4Iiwid2VicGFjazovL2xhYjEtZnJvbnRlbmQvLi9zcmMvY29tcG9uZW50cy9Ub2FzdC5zY3NzPzUwOTciLCJ3ZWJwYWNrOi8vbGFiMS1mcm9udGVuZC8uL3NyYy9jb21wb25lbnRzL1RvYXN0LnRzeCIsIndlYnBhY2s6Ly9sYWIxLWZyb250ZW5kLy4vc3JjL2pzeC9vYnNlcnZlci50cyIsIndlYnBhY2s6Ly9sYWIxLWZyb250ZW5kLy4vc3JjL2pzeC9wcmFnbWEudHMiLCJ3ZWJwYWNrOi8vbGFiMS1mcm9udGVuZC93ZWJwYWNrL2Jvb3RzdHJhcCIsIndlYnBhY2s6Ly9sYWIxLWZyb250ZW5kL3dlYnBhY2svcnVudGltZS9kZWZpbmUgcHJvcGVydHkgZ2V0dGVycyIsIndlYnBhY2s6Ly9sYWIxLWZyb250ZW5kL3dlYnBhY2svcnVudGltZS9oYXNPd25Qcm9wZXJ0eSBzaG9ydGhhbmQiLCJ3ZWJwYWNrOi8vbGFiMS1mcm9udGVuZC93ZWJwYWNrL3J1bnRpbWUvbWFrZSBuYW1lc3BhY2Ugb2JqZWN0Iiwid2VicGFjazovL2xhYjEtZnJvbnRlbmQvLi9zcmMvaW5kZXgudHMiXSwic291cmNlc0NvbnRlbnQiOlsiaW1wb3J0IGpzeCBmcm9tIFwiLi9qc3gvcHJhZ21hXCJcclxuaW1wb3J0IHtBcnJheU9ic2VydmVyLCBPYnNlcnZlcn0gZnJvbSBcIi4vanN4L29ic2VydmVyXCI7XHJcblxyXG5pbXBvcnQgVG9hc3QgZnJvbSBcIi4vY29tcG9uZW50cy9Ub2FzdFwiO1xyXG5cclxuY29uc3QgZXJyb3JzID0gbmV3IEFycmF5T2JzZXJ2ZXIoW1wiZXJyMVwiLCBcImVycjJcIiwgXCJlcnIzXCJdKTtcclxuXHJcbmNvbnN0IEFwcCA9ICgpID0+IHtcclxuICAgIHJldHVybiAoXHJcbiAgICAgICAgPGRpdj5cclxuICAgICAgICAgICAgPFRvYXN0IHRpdGxlPVwiRXJyb3JcIiBtZXNzYWdlPVwiYWJjXCIvPlxyXG4gICAgICAgICAgICA8VG9hc3QgdGl0bGU9XCJFcnJvclwiIG1lc3NhZ2U9XCJhYmNcIi8+XHJcbiAgICAgICAgICAgIDxUb2FzdCB0aXRsZT1cIkVycm9yXCIgbWVzc2FnZT1cImFiY1wiLz5cclxuICAgICAgICA8L2Rpdj5cclxuICAgIClcclxufVxyXG5cclxuZXhwb3J0IGRlZmF1bHQgQXBwIiwiLy8gZXh0cmFjdGVkIGJ5IG1pbmktY3NzLWV4dHJhY3QtcGx1Z2luXG5leHBvcnQge307IiwiaW1wb3J0IGpzeCBmcm9tIFwiLi4vanN4L3ByYWdtYVwiO1xyXG5pbXBvcnQgXCIuL1RvYXN0LnNjc3NcIlxyXG5cclxuaW50ZXJmYWNlIFRvYXN0UHJvcHMge1xyXG4gICAgdGl0bGU6IHN0cmluZztcclxuICAgIG1lc3NhZ2U6IHN0cmluZztcclxufVxyXG5cclxuZXhwb3J0IGRlZmF1bHQgZnVuY3Rpb24gVG9hc3Qoe3RpdGxlLCBtZXNzYWdlfTogVG9hc3RQcm9wcykge1xyXG4gICAgcmV0dXJuIDxkaXYgY2xhc3NOYW1lPVwidG9hc3RcIj5cclxuICAgICAgICA8aDE+e3RpdGxlfTwvaDE+XHJcbiAgICAgICAgPHA+e21lc3NhZ2V9PC9wPlxyXG4gICAgPC9kaXY+O1xyXG59OyIsInR5cGUgTGlzdGVuZXI8VD4gPSAodmFsdWU6IFQpID0+IHZvaWQ7XHJcblxyXG5cclxuY2xhc3MgT2JzZXJ2ZXI8VD4ge1xyXG4gICAgcHJpdmF0ZSBfdmFsdWU6IFQ7XHJcbiAgICBwcml2YXRlIF9saXN0ZW5lcnM6IFNldDxMaXN0ZW5lcjxUPj4gPSBuZXcgU2V0PExpc3RlbmVyPFQ+PigpO1xyXG5cclxuICAgIGNvbnN0cnVjdG9yKGluaXRpYWw6IFQpIHtcclxuICAgICAgICB0aGlzLl92YWx1ZSA9IGluaXRpYWw7XHJcbiAgICB9XHJcblxyXG4gICAgZ2V0IHZhbHVlKCkge1xyXG4gICAgICAgIHJldHVybiB0aGlzLl92YWx1ZTtcclxuICAgIH1cclxuXHJcbiAgICBzZXQgdmFsdWUobmV3VmFsdWU6IFQpIHtcclxuICAgICAgICBpZiAodGhpcy5fdmFsdWUgIT09IG5ld1ZhbHVlKSB7XHJcbiAgICAgICAgICAgIHRoaXMuX3ZhbHVlID0gbmV3VmFsdWU7XHJcblxyXG4gICAgICAgICAgICB0aGlzLm5vdGlmeShuZXdWYWx1ZSlcclxuICAgICAgICB9XHJcbiAgICB9XHJcblxyXG4gICAgb25DaGFuZ2UobGlzdGVuZXI6IExpc3RlbmVyPFQ+KSB7XHJcbiAgICAgICAgdGhpcy5fbGlzdGVuZXJzLmFkZChsaXN0ZW5lcik7XHJcbiAgICAgICAgbGlzdGVuZXIodGhpcy5fdmFsdWUpO1xyXG4gICAgfVxyXG5cclxuICAgIG5vdGlmeShuZXdWYWx1ZTogVCkge1xyXG4gICAgICAgIHRoaXMuX2xpc3RlbmVycy5mb3JFYWNoKGxpc3RlbmVyID0+IGxpc3RlbmVyKG5ld1ZhbHVlKSk7XHJcbiAgICB9XHJcbn1cclxuXHJcbmNsYXNzIEFycmF5T2JzZXJ2ZXI8VD4gZXh0ZW5kcyBPYnNlcnZlcjxUW10+IHtcclxuICAgIHByaXZhdGUgcHJveHk6IFRbXTtcclxuXHJcbiAgICBjb25zdHJ1Y3Rvcihpbml0aWFsOiBUW10pIHtcclxuICAgICAgICBzdXBlcihpbml0aWFsKTtcclxuXHJcbiAgICAgICAgdGhpcy5wcm94eSA9IHRoaXMuY3JlYXRlUHJveHkoaW5pdGlhbClcclxuICAgIH1cclxuXHJcbiAgICBnZXQgdmFsdWUoKSB7XHJcbiAgICAgICAgcmV0dXJuIHRoaXMucHJveHk7XHJcbiAgICB9XHJcblxyXG4gICAgcHJpdmF0ZSBjcmVhdGVQcm94eShhcnJheTogVFtdKTogVFtdIHtcclxuICAgICAgICBjb25zdCBtZXRob2RzVG9JbnRlcmNlcHQgPSBbXHJcbiAgICAgICAgICAgICdwdXNoJywgJ3BvcCcsICdzaGlmdCcsICd1bnNoaWZ0JywgJ3NwbGljZScsXHJcbiAgICAgICAgICAgICdzb3J0JywgJ3JldmVyc2UnLCAnY29weVdpdGhpbicsICdmaWxsJ1xyXG4gICAgICAgIF07XHJcblxyXG4gICAgICAgIHJldHVybiBuZXcgUHJveHkoYXJyYXksIHtcclxuICAgICAgICAgICAgZ2V0OiAodGFyZ2V0LCBwcm9wKSA9PiB7XHJcbiAgICAgICAgICAgICAgICBpZiAobWV0aG9kc1RvSW50ZXJjZXB0LmluY2x1ZGVzKHByb3AgYXMgc3RyaW5nKSkge1xyXG4gICAgICAgICAgICAgICAgICAgIHJldHVybiAoLi4uYXJnczogYW55W10pID0+IHtcclxuICAgICAgICAgICAgICAgICAgICAgICAgY29uc3QgcmVzdWx0ID0gKHRhcmdldCBhcyBhbnkpW3Byb3BdKC4uLmFyZ3MpO1xyXG4gICAgICAgICAgICAgICAgICAgICAgICB0aGlzLm5vdGlmeShbLi4udGFyZ2V0XSk7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHJldHVybiByZXN1bHQ7XHJcbiAgICAgICAgICAgICAgICAgICAgfTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIHJldHVybiB0YXJnZXRbcHJvcCBhcyBhbnldO1xyXG4gICAgICAgICAgICB9LFxyXG4gICAgICAgICAgICBzZXQ6ICh0YXJnZXQsIHByb3AsIHZhbHVlKSA9PiB7XHJcbiAgICAgICAgICAgICAgICBjb25zdCBudW1lcmljUHJvcCA9IE51bWJlcihwcm9wKTtcclxuICAgICAgICAgICAgICAgIGlmICghaXNOYU4obnVtZXJpY1Byb3ApIHx8IHByb3AgPT09ICdsZW5ndGgnKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgY29uc3Qgb2xkVmFsdWUgPSB0YXJnZXRbcHJvcCBhcyBhbnldO1xyXG4gICAgICAgICAgICAgICAgICAgIHRhcmdldFtwcm9wIGFzIGFueV0gPSB2YWx1ZTtcclxuICAgICAgICAgICAgICAgICAgICBpZiAob2xkVmFsdWUgIT09IHZhbHVlKSB7XHJcbiAgICAgICAgICAgICAgICAgICAgICAgIHRoaXMubm90aWZ5KFsuLi50YXJnZXRdKTtcclxuICAgICAgICAgICAgICAgICAgICB9XHJcbiAgICAgICAgICAgICAgICB9IGVsc2Uge1xyXG4gICAgICAgICAgICAgICAgICAgIHRhcmdldFtwcm9wIGFzIGFueV0gPSB2YWx1ZTtcclxuICAgICAgICAgICAgICAgIH1cclxuICAgICAgICAgICAgICAgIHJldHVybiB0cnVlO1xyXG4gICAgICAgICAgICB9XHJcbiAgICAgICAgfSk7XHJcbiAgICB9XHJcbn1cclxuXHJcbmV4cG9ydCB7QXJyYXlPYnNlcnZlciwgT2JzZXJ2ZXJ9OyIsImltcG9ydCB7T2JzZXJ2ZXIsIEFycmF5T2JzZXJ2ZXJ9IGZyb20gXCIuL29ic2VydmVyXCI7XHJcbmltcG9ydCB7SFRNTEF0dHJpYnV0ZXN9IGZyb20gXCIuL3R5cGVzXCI7XHJcblxyXG50eXBlIENoaWxkID1cclxuICAgIHwgTm9kZVxyXG4gICAgfCAoKHByb3BlcnRpZXM6IFByb3BlcnRpZXMpID0+IHN0cmluZyB8IE5vZGUpXHJcbiAgICB8IHN0cmluZ1xyXG5cclxudHlwZSBQcm9wZXJ0aWVzID0gSFRNTEF0dHJpYnV0ZXNcclxuXHJcbnR5cGUgSlNYRWxlbWVudCA9XHJcbiAgICB8IHN0cmluZ1xyXG4gICAgfCAoKHByb3BlcnRpZXM6IFByb3BlcnRpZXMsIGNoaWxkcmVuOiBDaGlsZFtdKSA9PiBOb2RlKVxyXG5cclxuZnVuY3Rpb24gcGFyc2VDaGlsZHJlbihjaGlsZHJlbjogQ2hpbGRbXSkge1xyXG4gICAgcmV0dXJuIGNoaWxkcmVuLm1hcChjaGlsZCA9PiB7XHJcbiAgICAgICAgaWYgKHR5cGVvZiBjaGlsZCA9PT0gJ3N0cmluZycpIHtcclxuICAgICAgICAgICAgcmV0dXJuIGRvY3VtZW50LmNyZWF0ZVRleHROb2RlKGNoaWxkKTtcclxuICAgICAgICB9XHJcbiAgICAgICAgcmV0dXJuIGNoaWxkO1xyXG4gICAgfSlcclxufVxyXG5cclxuXHJcbmZ1bmN0aW9uIHBhcnNlTm9kZSh0YWc6IHN0cmluZywgcHJvcGVydGllczogUHJvcGVydGllcywgY2hpbGRyZW46IENoaWxkW10pOiBOb2RlIHtcclxuICAgIGNvbnN0IGVsID0gZG9jdW1lbnQuY3JlYXRlRWxlbWVudCh0YWcpO1xyXG5cclxuICAgIGlmIChwcm9wZXJ0aWVzLm1vZGVsICE9PSB1bmRlZmluZWQpIHtcclxuICAgICAgICBpZiAodHlwZW9mIHByb3BlcnRpZXMubW9kZWwgPT09ICdzdHJpbmcnKSB7XHJcbiAgICAgICAgICAgIGNvbnNvbGUubG9nKHByb3BlcnRpZXNbcHJvcGVydGllcy5tb2RlbF0pXHJcbiAgICAgICAgICAgIGVsLmlubmVyVGV4dCA9IHByb3BlcnRpZXNbcHJvcGVydGllcy5tb2RlbF07XHJcbiAgICAgICAgfSBlbHNlIHtcclxuICAgICAgICAgICAgY29uc3QgbW9kZWw6IE9ic2VydmVyPGFueT4gPSBwcm9wZXJ0aWVzLm1vZGVsO1xyXG4gICAgICAgICAgICBtb2RlbC5vbkNoYW5nZSgodmFsdWUpID0+IHtcclxuICAgICAgICAgICAgICAgIGlmIChlbCBpbnN0YW5jZW9mIEhUTUxJbnB1dEVsZW1lbnQpIHtcclxuICAgICAgICAgICAgICAgICAgICBlbC52YWx1ZSA9IHZhbHVlO1xyXG4gICAgICAgICAgICAgICAgfSBlbHNlIGVsLmlubmVyVGV4dCA9IHZhbHVlO1xyXG4gICAgICAgICAgICB9KVxyXG4gICAgICAgIH1cclxuICAgIH1cclxuXHJcbiAgICBmb3IgKGNvbnN0IGtleSBpbiBwcm9wZXJ0aWVzKSB7XHJcbiAgICAgICAgY29uc3QgdmFsdWUgPSBwcm9wZXJ0aWVzW2tleV07XHJcblxyXG4gICAgICAgIC8vIGlmICh0eXBlb2YgdmFsdWUgPT09IFwiZnVuY3Rpb25cIikge1xyXG4gICAgICAgIC8vICAgICBlbFtrZXldID0gKGV2ZW50OiBFdmVudCkgPT4ge1xyXG4gICAgICAgIC8vICAgICAgICAgLy8g0J7Qv9GA0LXQtNC10LvRj9C10Lwg0YHQuNCz0L3QsNGC0YPRgNGDINGE0YPQvdC60YbQuNC4INC4INCy0YvQt9GL0LLQsNC10LwgYWNjb3JkaW5nbHlcclxuICAgICAgICAvLyAgICAgICAgIGNvbnN0IGZ1bmN0aW9uTGVuZ3RoID0gdmFsdWUubGVuZ3RoO1xyXG4gICAgICAgIC8vXHJcbiAgICAgICAgLy8gICAgICAgICBpZiAodmFsdWUubGVuZ3RoID09PSAxKSB7XHJcbiAgICAgICAgLy8gICAgICAgICAgICAgcmV0dXJuIHZhbHVlKHtcclxuICAgICAgICAvLyAgICAgICAgICAgICAgICAgZXZlbnQ6IGV2ZW50LFxyXG4gICAgICAgIC8vICAgICAgICAgICAgICAgICBpdGVtOiBwcm9wZXJ0aWVzWydkYXRhLWl0ZW0nXSxcclxuICAgICAgICAvLyAgICAgICAgICAgICB9KTtcclxuICAgICAgICAvLyAgICAgICAgIH1cclxuICAgICAgICAvL1xyXG4gICAgICAgIC8vICAgICAgICAgcmV0dXJuIHZhbHVlKClcclxuICAgICAgICAvLyAgICAgfTtcclxuICAgICAgICAvLyB9IGVsc2VcclxuICAgICAgICBlbFtrZXldID0gcHJvcGVydGllc1trZXldO1xyXG5cclxuICAgIH1cclxuXHJcbiAgICBwYXJzZUNoaWxkcmVuKGNoaWxkcmVuKS5mb3JFYWNoKGNoaWxkID0+IHtcclxuICAgICAgICBpZiAodHlwZW9mIGNoaWxkID09PSAnZnVuY3Rpb24nKSB7XHJcbiAgICAgICAgICAgIGNvbnN0IGNoaWxkMiA9IGNoaWxkKHByb3BlcnRpZXMpO1xyXG5cclxuICAgICAgICAgICAgaWYgKHR5cGVvZiBjaGlsZDIgPT09ICdzdHJpbmcnKSB7XHJcbiAgICAgICAgICAgICAgICBlbC5hcHBlbmRDaGlsZChkb2N1bWVudC5jcmVhdGVUZXh0Tm9kZShjaGlsZDIpKVxyXG4gICAgICAgICAgICB9IGVsc2UgZWwuYXBwZW5kQ2hpbGQoY2hpbGQyKTtcclxuICAgICAgICB9IGVsc2Uge1xyXG4gICAgICAgICAgICBlbC5hcHBlbmRDaGlsZChjaGlsZCk7XHJcbiAgICAgICAgfVxyXG4gICAgfSk7XHJcbiAgICByZXR1cm4gZWw7XHJcbn1cclxuXHJcbmZ1bmN0aW9uIGpzeChlbGVtZW50OiBKU1hFbGVtZW50LCBwcm9wZXJ0aWVzPzogUHJvcGVydGllcywgLi4uY2hpbGRyZW46IENoaWxkW10pOiBOb2RlIHtcclxuICAgIHByb3BlcnRpZXMgPSBwcm9wZXJ0aWVzIHx8IHt9O1xyXG5cclxuICAgIGlmICh0eXBlb2YgZWxlbWVudCA9PT0gJ2Z1bmN0aW9uJykge1xyXG4gICAgICAgIHJldHVybiBlbGVtZW50KHByb3BlcnRpZXMsIGNoaWxkcmVuKTtcclxuICAgIH1cclxuXHJcbiAgICBpZiAocHJvcGVydGllcy5mb3IgIT09IHVuZGVmaW5lZCkge1xyXG4gICAgICAgIGNvbnN0IGl0ZXIgPSBwcm9wZXJ0aWVzLmZvcjtcclxuICAgICAgICBjb25zdCBrZXkgPSBwcm9wZXJ0aWVzLmZvcktleSB8fCBcIml0ZW1cIlxyXG4gICAgICAgIGNvbnN0IGZyYWdtZW50ID0gZG9jdW1lbnQuY3JlYXRlRG9jdW1lbnRGcmFnbWVudCgpXHJcblxyXG4gICAgICAgIGlmIChpdGVyIGluc3RhbmNlb2YgQXJyYXlPYnNlcnZlcikge1xyXG4gICAgICAgICAgICBsZXQgbm9kZXM6IE5vZGVbXSA9IFtdO1xyXG5cclxuICAgICAgICAgICAgY29uc3QgcmVyZW5kZXIgPSAoaXRlbXM6IGFueVtdKSA9PiB7XHJcbiAgICAgICAgICAgICAgICBsZXQgcGFyZW50IDogUGFyZW50Tm9kZTtcclxuXHJcbiAgICAgICAgICAgICAgICBub2Rlcy5mb3JFYWNoKChjaGlsZDogTm9kZSkgPT4ge1xyXG4gICAgICAgICAgICAgICAgICAgIHBhcmVudCA9IGNoaWxkLnBhcmVudE5vZGU7XHJcbiAgICAgICAgICAgICAgICAgICAgcGFyZW50LnJlbW92ZUNoaWxkKGNoaWxkKTtcclxuICAgICAgICAgICAgICAgIH0pXHJcbiAgICAgICAgICAgICAgICBub2RlcyA9IFtdXHJcblxyXG4gICAgICAgICAgICAgICAgaXRlbXMuZm9yRWFjaChpdGVtID0+IHtcclxuICAgICAgICAgICAgICAgICAgICBwcm9wZXJ0aWVzW2tleV0gPSBpdGVtXHJcbiAgICAgICAgICAgICAgICAgICAgbm9kZXMucHVzaChwYXJzZU5vZGUoZWxlbWVudCwgcHJvcGVydGllcywgY2hpbGRyZW4uZmxhdCgpKSlcclxuICAgICAgICAgICAgICAgIH0pXHJcblxyXG4gICAgICAgICAgICAgICAgbm9kZXMuZm9yRWFjaChub2RlID0+IHtcclxuICAgICAgICAgICAgICAgICAgICBpZiAocGFyZW50KSBwYXJlbnQuYXBwZW5kQ2hpbGQobm9kZSlcclxuICAgICAgICAgICAgICAgICAgICBlbHNlIGZyYWdtZW50LmFwcGVuZENoaWxkKG5vZGUpXHJcbiAgICAgICAgICAgICAgICB9KVxyXG4gICAgICAgICAgICB9XHJcblxyXG4gICAgICAgICAgICBpdGVyLm9uQ2hhbmdlKCh2YWx1ZSkgPT4ge1xyXG4gICAgICAgICAgICAgICAgcmVyZW5kZXIoaXRlci52YWx1ZSlcclxuICAgICAgICAgICAgfSlcclxuXHJcbiAgICAgICAgfSBlbHNlIHtcclxuICAgICAgICAgICAgQXJyYXkuZnJvbShpdGVyKVxyXG4gICAgICAgICAgICAgICAgLmZvckVhY2goZWwgPT4ge1xyXG4gICAgICAgICAgICAgICAgICAgIHByb3BlcnRpZXNba2V5XSA9IGVsXHJcbiAgICAgICAgICAgICAgICAgICAgZnJhZ21lbnQuYXBwZW5kQ2hpbGQocGFyc2VOb2RlKGVsZW1lbnQsIHByb3BlcnRpZXMsIGNoaWxkcmVuLmZsYXQoKSkpXHJcbiAgICAgICAgICAgICAgICB9KVxyXG4gICAgICAgIH1cclxuXHJcbiAgICAgICAgcmV0dXJuIGZyYWdtZW50XHJcbiAgICB9XHJcblxyXG4gICAgcmV0dXJuIHBhcnNlTm9kZShlbGVtZW50LCBwcm9wZXJ0aWVzLCBjaGlsZHJlbi5mbGF0KCkpO1xyXG59XHJcblxyXG5leHBvcnQgZGVmYXVsdCBqc3g7IiwiLy8gVGhlIG1vZHVsZSBjYWNoZVxudmFyIF9fd2VicGFja19tb2R1bGVfY2FjaGVfXyA9IHt9O1xuXG4vLyBUaGUgcmVxdWlyZSBmdW5jdGlvblxuZnVuY3Rpb24gX193ZWJwYWNrX3JlcXVpcmVfXyhtb2R1bGVJZCkge1xuXHQvLyBDaGVjayBpZiBtb2R1bGUgaXMgaW4gY2FjaGVcblx0dmFyIGNhY2hlZE1vZHVsZSA9IF9fd2VicGFja19tb2R1bGVfY2FjaGVfX1ttb2R1bGVJZF07XG5cdGlmIChjYWNoZWRNb2R1bGUgIT09IHVuZGVmaW5lZCkge1xuXHRcdHJldHVybiBjYWNoZWRNb2R1bGUuZXhwb3J0cztcblx0fVxuXHQvLyBDcmVhdGUgYSBuZXcgbW9kdWxlIChhbmQgcHV0IGl0IGludG8gdGhlIGNhY2hlKVxuXHR2YXIgbW9kdWxlID0gX193ZWJwYWNrX21vZHVsZV9jYWNoZV9fW21vZHVsZUlkXSA9IHtcblx0XHQvLyBubyBtb2R1bGUuaWQgbmVlZGVkXG5cdFx0Ly8gbm8gbW9kdWxlLmxvYWRlZCBuZWVkZWRcblx0XHRleHBvcnRzOiB7fVxuXHR9O1xuXG5cdC8vIEV4ZWN1dGUgdGhlIG1vZHVsZSBmdW5jdGlvblxuXHRfX3dlYnBhY2tfbW9kdWxlc19fW21vZHVsZUlkXShtb2R1bGUsIG1vZHVsZS5leHBvcnRzLCBfX3dlYnBhY2tfcmVxdWlyZV9fKTtcblxuXHQvLyBSZXR1cm4gdGhlIGV4cG9ydHMgb2YgdGhlIG1vZHVsZVxuXHRyZXR1cm4gbW9kdWxlLmV4cG9ydHM7XG59XG5cbiIsIi8vIGRlZmluZSBnZXR0ZXIgZnVuY3Rpb25zIGZvciBoYXJtb255IGV4cG9ydHNcbl9fd2VicGFja19yZXF1aXJlX18uZCA9IChleHBvcnRzLCBkZWZpbml0aW9uKSA9PiB7XG5cdGZvcih2YXIga2V5IGluIGRlZmluaXRpb24pIHtcblx0XHRpZihfX3dlYnBhY2tfcmVxdWlyZV9fLm8oZGVmaW5pdGlvbiwga2V5KSAmJiAhX193ZWJwYWNrX3JlcXVpcmVfXy5vKGV4cG9ydHMsIGtleSkpIHtcblx0XHRcdE9iamVjdC5kZWZpbmVQcm9wZXJ0eShleHBvcnRzLCBrZXksIHsgZW51bWVyYWJsZTogdHJ1ZSwgZ2V0OiBkZWZpbml0aW9uW2tleV0gfSk7XG5cdFx0fVxuXHR9XG59OyIsIl9fd2VicGFja19yZXF1aXJlX18ubyA9IChvYmosIHByb3ApID0+IChPYmplY3QucHJvdG90eXBlLmhhc093blByb3BlcnR5LmNhbGwob2JqLCBwcm9wKSkiLCIvLyBkZWZpbmUgX19lc01vZHVsZSBvbiBleHBvcnRzXG5fX3dlYnBhY2tfcmVxdWlyZV9fLnIgPSAoZXhwb3J0cykgPT4ge1xuXHRpZih0eXBlb2YgU3ltYm9sICE9PSAndW5kZWZpbmVkJyAmJiBTeW1ib2wudG9TdHJpbmdUYWcpIHtcblx0XHRPYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgU3ltYm9sLnRvU3RyaW5nVGFnLCB7IHZhbHVlOiAnTW9kdWxlJyB9KTtcblx0fVxuXHRPYmplY3QuZGVmaW5lUHJvcGVydHkoZXhwb3J0cywgJ19fZXNNb2R1bGUnLCB7IHZhbHVlOiB0cnVlIH0pO1xufTsiLCJpbXBvcnQgQXBwIGZyb20gXCIuL0FwcFwiO1xyXG5cclxuXHJcbmNvbnN0IHJvb3QgPSBkb2N1bWVudC5nZXRFbGVtZW50QnlJZChcInJvb3RcIik7XHJcbnJvb3QuYXBwZW5kQ2hpbGQoQXBwKCkpXHJcbiJdLCJuYW1lcyI6W10sInNvdXJjZVJvb3QiOiIifQ==